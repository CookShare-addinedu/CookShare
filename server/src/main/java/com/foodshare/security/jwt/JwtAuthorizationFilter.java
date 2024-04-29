package com.foodshare.security.jwt;

import com.foodshare.domain.User;
import com.foodshare.security.dto.CustomUserDetails;
import com.foodshare.security.service.RedisService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.foodshare.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.foodshare.security.jwt.SecurityConstants.REFRESH_EXPIRATION_TIME;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Value("${com.foodshare.jwt.secret-key}")
	private String secretKey;
	private final JwtUtil jwtUtil;
	private final RedisService redisService;

	@Autowired
	public JwtAuthorizationFilter(JwtUtil jwtUtil, RedisService redisService) {
		this.jwtUtil = jwtUtil;
		this.redisService = redisService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		String path = request.getRequestURI();

		// 로그아웃 경로에 대한 요청은 필터링하지 않음
		if (path.startsWith("/api/user/logout")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = request.getHeader("Authorization");
		if (token != null && redisService.isTokenBlacklisted(token)) {
			throw new SecurityException("블랙리스트된 토큰입니다.");
		}

		boolean shouldContinue = true;

		if (authorization != null && authorization.startsWith("Bearer ")) {
			token = authorization.substring(7);
			try {
				if (!jwtUtil.isExpired(token)) {
					processAuthentication(token, response);
				} else {
					// 만약 액세스 토큰이 만료되었다면, 리프레시 토큰을 검사합니다.
					handleExpiredToken(request, response, filterChain);
					shouldContinue = false; // 리프레시 토큰으로 새 액세스 토큰을 발급받는 경우, 이어서 처리하지 않고 중단
				}
			} catch (ExpiredJwtException e) {
				// 리프레시 토큰 처리 로직을 호출
				handleExpiredToken(request, response, filterChain);
				shouldContinue = false;
			}
		}
		if (shouldContinue && !response.isCommitted()) {
			filterChain.doFilter(request, response);
		}
	}

	private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {
		Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
			.filter(cookie -> "Refresh-Token".equals(cookie.getName()))
			.findFirst()
			.orElse(null);

		if (refreshTokenCookie != null && !jwtUtil.isRefreshTokenExpired(refreshTokenCookie.getValue())) {
			renewTokens(refreshTokenCookie.getValue(), response);
			if (!response.isCommitted()) {
				filterChain.doFilter(request, response);
			}
		} else {
			unauthorizedResponse(response, refreshTokenCookie == null ? "리프레쉬 토큰이 없습니다." : "리프레쉬 토큰이 만료 되었습니다.");
		}

	}

	private void renewTokens(String NewrefreshToken, HttpServletResponse response) throws IOException {
		System.out.println("리프레시토큰 출력 " + NewrefreshToken);

		if (NewrefreshToken.startsWith("Bearer")) {
			NewrefreshToken = NewrefreshToken.substring("Bearer".length()).trim();
		}
		System.out.println("Bearer 접두사 제거 후: " + NewrefreshToken);

		// '+'를 '%2B'로 치환하여 디코딩
		NewrefreshToken = NewrefreshToken.replace("%2B", "+");
		String NewrefreshToken2 = URLDecoder.decode(NewrefreshToken, StandardCharsets.UTF_8.toString());
		System.out.println("디코딩 후: " + NewrefreshToken2);

		Long userId = jwtUtil.getUserId(NewrefreshToken2);
		System.out.println("새userId: " + userId);
		String username = jwtUtil.getMobileNumber(NewrefreshToken2);
		System.out.println("새username: " + username);
		String role = jwtUtil.getRole(NewrefreshToken2);
		String location = jwtUtil.getLocation(NewrefreshToken2);
		String nickName = jwtUtil.getNickName(NewrefreshToken2);

		String newAccessToken = jwtUtil.createAccessToken(userId, username, role, location, nickName);
		String newRefreshToken = jwtUtil.createRefreshToken(userId, username, role, location, nickName);
		long accessTokenExpirationMs = System.currentTimeMillis() + EXPIRATION_TIME;
		long refreshTokenExpirationMs = System.currentTimeMillis() + REFRESH_EXPIRATION_TIME;

		// 쿠키에 새 리프레시 토큰 설정 (인코딩)
		String encodedNewRefreshToken = URLEncoder.encode("Bearer " + newRefreshToken,
			StandardCharsets.UTF_8.toString());
		Cookie refreshCookie = new Cookie("Refresh-Token", encodedNewRefreshToken);

		// 테스트용 :false ,  원래는 : true
		refreshCookie.setHttpOnly(false);
		refreshCookie.setSecure(false);
		refreshCookie.setPath("/");
		response.addCookie(refreshCookie);

		// 서버 : Redis에 refreshToken 저장
		System.out.println("토큰발급한 번호: " + username);
		// Redis에 다시 저장
		redisService.saveToken(username, newRefreshToken, REFRESH_EXPIRATION_TIME);

		// 헤더에 새 토큰으로 변경
		response.addHeader("Authorization", "Bearer " + newAccessToken);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // ISO 8601 형식

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("새로운 엑세스토큰(accessToken)", newAccessToken);
		responseData.put("새로운 리프레시토큰(refreshToken)", newRefreshToken);
		responseData.put("새로운 엑세스토큰유효기간(accessTokenExpiresIn)", dateFormat.format(new Date(accessTokenExpirationMs)));
		responseData.put("새로운 리프레시토큰유효기간(refreshTokenExpiresIn)",
			dateFormat.format(new Date(refreshTokenExpirationMs)));
		responseData.put("role", role);

		response.setContentType("application/json; charset=utf8");
		response.getWriter().write("{\"message\": \"새로운 토큰이 발급되었습니다.\"}" + responseData);
		System.out.println(responseData);
		response.getWriter().flush();
	}

	private void processAuthentication(String token, HttpServletResponse response) throws IOException {
		String username = jwtUtil.getUsernameFromToken(token);
		Long userId = jwtUtil.getUserId(token);
		String role = jwtUtil.getRole(token);
		User user = new User();
		user.setUserId(userId);
		user.setMobileNumber(username);
		user.setRole(role);
		CustomUserDetails userDetails = new CustomUserDetails(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
			userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		if (!response.isCommitted()) {
			response.setContentType("application/json; charset=utf8");
			response.getWriter().write("{\"message\": \"토큰이 유효하고 사용자가 인증되었습니다.\"}");
			response.getWriter().flush();
		}
	}

	private void unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json; charset=utf8");
		response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
		response.getWriter().flush();
	}
}
