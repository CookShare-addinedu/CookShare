//package com.foodshare.security.controller;
//
//import com.foodshare.security.service.RedisService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//public class LogoutController {
//
//    @Value("${com.foodshare.jwt.secret-key}")
//    private String secretKey;
//
//    @Autowired
//    private RedisService redisService;
//
//    @DeleteMapping("/api/user/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7); // "Bearer " 다음부터 토큰 시작
//            String mobileNumber = extractMobileNumberFromToken(token); // 토큰에서 mobileNumber 추출
//
//            if (mobileNumber != null) {
//                redisService.deleteToken(mobileNumber); // Redis에서 refreshToken 삭제
//                removeCookie("Refresh-Token", request, response); // 클라이언트 측 쿠키 삭제
//
//                // 토큰 블랙리스트 처리
//                // String remainingTime = getRemainingTime(token);
//                // long duration = parseDuration(remainingTime); // 이 메서드는 시간 문자열을 초로 변환 (아래 구현 참조
//                long duration = getRemainingTime(token);
//                redisService.blacklistToken(token, duration);
//                System.out.println("토큰(DeleteMapping): " + token);
//                System.out.println("기간(DeleteMapping): " + duration);
//                return "로그아웃 성공: 모바일 번호로 인증되었습니다. (쿠키 제거 및 Redis 블랙리스트 처리)";
//            }
//        }
//        catch (SignatureException e) {
//            System.err.println("JWT 서명 검증 실패: " + e.getMessage());
//            return "유효하지 않은 토큰으로 로그아웃 실패";
//        }
//    }
//
//    private void removeCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(cookieName)) {
//                    cookie.setValue("");
//                    cookie.setPath("/");
//                    cookie.setMaxAge(0); // 쿠키 만료
//                    response.addCookie(cookie);
//                }
//            }
//        }
//    }
//
//    private String extractMobileNumberFromToken(String token) {
//         Claims claims = Jwts.parser()
//                .setSigningKey(secretKey.getBytes())
//                .parseClaimsJws(token)
//                .getBody();
//         return claims.get("mobileNumber", String.class);
//    }
//
//
//    public long getRemainingTime(String token) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//            Date expiration = claims.getExpiration();
//            long duration = expiration.getTime() - System.currentTimeMillis(); // 만료 시간과 현재 시간의 차이
//            System.out.println("토큰(getRemainingTime): " + token);
//            System.out.println("기간(getRemainingTime): " + duration);
//            return Math.max(duration / 1000, 1); // 최소 1초를 보장
//        } catch (JwtException e) {
//            System.err.println("JWT processing error: " + e.getMessage());
//            return 1; // 오류 시 기본적으로 1초 반환
//        }
//    }
//
//    private long parseDuration(String duration) {
//        if (duration != null) {
//            String[] parts = duration.split(" ");
//            long hours = Long.parseLong(parts[0].replace("시", ""));
//            long minutes = Long.parseLong(parts[1].replace("분", ""));
//            long seconds = Long.parseLong(parts[2].replace("초", ""));
//            return hours * 3600 + minutes * 60 + seconds;
//        }
//        return 0; // Default fallback
//    }
//
//    private String formatDuration(long durationMillis) {
//        long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60;
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60;
//
//        return String.format("%d시 %d분 %d초", hours, minutes, seconds);
//    }
//}
//



package com.foodshare.security.controller;

import com.foodshare.security.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
public class LogoutController {

    @Value("${com.foodshare.jwt.secret-key}")
    private String secretKeyEncoded;

    @Autowired
    private RedisService redisService;

    @DeleteMapping("/api/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String mobileNumber = extractMobileNumberFromToken(token);
                if (mobileNumber != null) {
                    redisService.deleteToken(mobileNumber);
                    removeCookie("Refresh-Token", request, response);

                    long duration = getRemainingTime(token);
                    redisService.blacklistToken(token, duration);

                    return "로그아웃 성공: 모바일 번호로 인증되었습니다. (쿠키 제거 및 Redis 블랙리스트 처리)";
                }
            } catch (SignatureException e) {
                System.err.println("JWT 서명 검증 실패: " + e.getMessage());
                return "유효하지 않은 토큰으로 로그아웃 실패";
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
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    private String extractMobileNumberFromToken(String token) throws SignatureException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyEncoded.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("mobileNumber", String.class);
    }

    public long getRemainingTime(String token) throws SignatureException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyEncoded.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        long duration = expiration.getTime() - System.currentTimeMillis();
        return Math.max(duration / 1000, 1);
    }
}

