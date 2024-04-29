package com.foodshare.security.controller;

import com.foodshare.security.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class LogoutController {

    @Value("${com.foodshare.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private RedisService redisService;

    @DeleteMapping("/api/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 다음부터 토큰 시작
            String mobileNumber = extractMobileNumberFromToken(token); // 토큰에서 mobileNumber 추출

            if (mobileNumber != null) {
                redisService.deleteToken(mobileNumber); // Redis에서 refreshToken 삭제
                removeCookie("Refresh-Token", request, response); // 클라이언트 측 쿠키 삭제
                return "로그아웃 성공: 모바일 번호로 인증되었습니다. (쿠키 제거 및 Redis 블랙리스트 처리)";
            }
        }
        return "유효하지 않은 로그아웃 요청입니다.";
    }

    private void removeCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // 쿠키 만료
                    response.addCookie(cookie);
                }
            }
        }
    }

    private String extractMobileNumberFromToken(String token) {
         Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("mobileNumber", String.class);
    }

}

