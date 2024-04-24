package com.foodshare.security.jwt;

import com.foodshare.domain.User;
import com.foodshare.security.service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.foodshare.security.service.RedisService;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProp jwtProp;
    private final RedisService redisService;

    @Autowired // JwtProp를 주입받기 위한 생성자입니다.
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProp jwtProp, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtProp = jwtProp;
        this.redisService =redisService;
        setFilterProcessesUrl("/api/login"); // 로그인 요청을 처리할 URL 지정
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("mobileNumber");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        // JWT 생성 로직
        String token = Jwts.builder()
                .setSubject(user.getMobileNumber())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(jwtProp.getSecretKey().getBytes()), SignatureAlgorithm.HS512)
                .compact();

        // 리프레시 토큰 생성 및 저장
        String refreshToken = UUID.randomUUID().toString();
        redisService.setKeyValueWithExpire("refreshToken:" + user.getMobileNumber(),
                                                refreshToken,
                                                SecurityConstants.REFRESH_EXPIRATION_TIME,
                                                 TimeUnit.SECONDS);


        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + " " + token);
        response.addHeader(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);

        //logger.info("Refresh token for user {} stored in Redis.", user.getMobileNumber());
    }
}

