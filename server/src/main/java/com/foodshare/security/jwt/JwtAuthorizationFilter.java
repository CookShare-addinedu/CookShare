package com.foodshare.security.jwt;

import com.foodshare.domain.User;
import com.foodshare.security.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    @Value("${com.foodshare.jwt.secret-key}")
    private String secretKey;
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                if (!jwtUtil.isExpired(token)) {
                    Date expiration = jwtUtil.getTokenExpiration(token);
                    long expiresIn = expiration.getTime() - System.currentTimeMillis();
                    long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(expiresIn);

                    String username = jwtUtil.getMobileNumber(token);
                    String role = jwtUtil.getRole(token);

                    User user = new User();
                    user.setMobileNumber(username);
                    user.setRole(role);

                    CustomUserDetails customUserDetails = new CustomUserDetails(user);
                    Authentication authToken = new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    response.setContentType("application/json; charset=utf8");
                    String responseMessage = String.format("{\"message\": \"토큰이 유효합니다. 남은 시간: %d 분\"}", minutesLeft);
                    response.getWriter().write(responseMessage);
                    response.getWriter().flush();
                    return; // 필터 체인 진행 중지하여 추가 처리 방지
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json; charset=utf8");
                    response.getWriter().write("{\"error\": \"만료된 토큰입니다.\"}");
                    response.getWriter().flush();
                    return; // 필터 체인 진행 중지
                }
            } catch (SignatureException | ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=utf8");
                //response.getWriter().write(String.format("{\"error\": \"%s\"}", e.getMessage()));
                response.getWriter().write("{\"error\": \"만료된 토큰입니다.\"}");
                response.getWriter().flush();
                return; // 필터 체인 진행 중지
            }
        }

        filterChain.doFilter(request, response);
    }


}

