package com.foodshare.security.jwt;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodshare.security.dto.CustomUserDetails;
import com.foodshare.security.service.RedisService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import static com.foodshare.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.foodshare.security.jwt.SecurityConstants.REFRESH_EXPIRATION_TIME;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RedisService redisService;

	@Autowired
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager
		, JwtUtil jwtUtil
		, RedisService redisService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.redisService = redisService;
		this.setFilterProcessesUrl("/api/user/login");

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			// JSON 본문을 파싱하여 AuthenticationRequest 객체 생성
			AuthenticationRequest authRequest = new ObjectMapper().readValue(request.getInputStream(),
				AuthenticationRequest.class);
			String rawUsername = authRequest.getTel();
			// 전화번호에서 하이픈('-') 제거
			String username = rawUsername.replace("-", "");
			//String username = authRequest.getMobileNumber();
			String password = authRequest.getPassword();

			System.out.println("Attempting authentication for username: " + username);
			System.out.println("Attempting authentication for password: " + password);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
			return authenticationManager.authenticate(authenticationToken);
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse authentication request body", e);
		}
	}

	private Cookie createCookie(String name, String value, int maxAge, String path, boolean httpOnly, boolean secure) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge); // 쿠키의 유효 시간 설정
		cookie.setPath(path); // 쿠키 경로 설정
		cookie.setHttpOnly(httpOnly); // JavaScript를 통한 접근 방지
		cookie.setSecure(secure); // HTTPS를 통해서만 쿠키를 전송
		return cookie;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain, Authentication authentication) throws IOException, ServletException {

		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Long userId = customUserDetails.getUserId();
		String username = customUserDetails.getUsername();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

        String token = jwtUtil.createAccessToken(userId, username, role);
		String refreshToken = jwtUtil.createRefreshToken(userId,username);
		long accessTokenExpirationMs = System.currentTimeMillis() + EXPIRATION_TIME;
		long refreshTokenExpirationMs = System.currentTimeMillis() + REFRESH_EXPIRATION_TIME;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // ISO 8601 형식

		// 쿠키 생성 및 추가
		String encodedRefreshToken = URLEncoder.encode("Bearer " + refreshToken, StandardCharsets.UTF_8.toString());
		Cookie refreshCookie = new Cookie("Refresh-Token", encodedRefreshToken);
		// 테스트용 :false ,  원래는 : true
		refreshCookie.setHttpOnly(false);
		refreshCookie.setSecure(false);
		refreshCookie.setPath("/");
		response.addCookie(refreshCookie);

		// 토큰 세부 정보를 Map에 저장
		Map<String, Object> tokenDetails = new HashMap<>();
		tokenDetails.put("token", token);
		tokenDetails.put("리프레시토큰(refreshToken)", refreshToken);
		tokenDetails.put("엑세스토큰유효기간(accessTokenExpiresIn)", dateFormat.format(new Date(accessTokenExpirationMs)));
		tokenDetails.put("리프레시토큰유효기간(refreshTokenExpiresIn)", dateFormat.format(new Date(refreshTokenExpirationMs)));
		tokenDetails.put("role", role);

		response.addHeader("Authorization", "Bearer+" + token);

		// 서버 : Redis에 refreshToken 저장
		System.out.println("토큰발급한 번호: " + username);
		redisService.saveToken(username, refreshToken, REFRESH_EXPIRATION_TIME);

		// 콘솔 출력용도
		for (Map.Entry<String, Object> entry : tokenDetails.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}

		// 클라이언트 : 쿠키에 새 리프레시 토큰 설정
		//        Cookie refreshCookie = new Cookie("Refresh-Token", refreshToken);
		//        refreshCookie.setHttpOnly(true);
		//        refreshCookie.setSecure(true);
		//        refreshCookie.setPath("/");
		//        response.addCookie(refreshCookie);

		// 응답 본문을 JSON 형태로 설정
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().write(mapper.writeValueAsString(tokenDetails));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		//로그인 실패시 메소드
		//response.sendRedirect("/login.html?error=true");
		response.setStatus(401);
	}
}

