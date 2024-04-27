package com.foodshare.security.jwt;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    @Autowired
    public JwtUtil(JwtProp jwtProp) {
        this.secretKey = new SecretKeySpec(jwtProp.getSecretKey().getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
    }

    // 검증

//    public Long getUserId(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("userId", String.class);
//    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String userIdStr = claims.get("userId", String.class);  // String으로 userId 값을 가져옴
        return Long.parseLong(userIdStr);  // String을 Long으로 변환
    }



    public String getMobileNumber(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("mobileNumber", String.class);
    }

    public String getLocation(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("location", String.class);
    }

    public String getNickName(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("nickName", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // 엑세스 토큰 생성
    public String createAccessToken(Long userId, String mobileNumber, String role, String location, String nickName) {
        return Jwts.builder()
                .claim("userId", String.valueOf(userId))
                .claim("mobileNumber", mobileNumber)
                .claim("role", "ROLE_" + role.toUpperCase())
                .claim( "location", location)
                .claim("nickName", nickName )
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 발행시간
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))   // 소멸시간
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }
    // 리프레시 토큰 생성
    public String createRefreshToken(Long userId, String mobileNumber, String role, String location, String nickName) {
        return Jwts.builder()
                .claim("userId", String.valueOf(userId))
                .claim("mobileNumber", mobileNumber)
                .claim("role", "ROLE_" + role.toUpperCase())
                .claim( "location", location)
                .claim("nickName", nickName )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +  SecurityConstants.REFRESH_EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }

    public Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new RuntimeException("토큰이 유효하지 않습니다", e);
        }
    }

    public Date getTokenExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 이름(아이디) 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}