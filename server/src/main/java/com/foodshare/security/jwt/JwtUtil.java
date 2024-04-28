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
	public String getMobileNumber(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("mobilenumber", String.class);
	}

	public String getRole(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("role", String.class);
	}

	public Boolean isExpired(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration()
			.before(new Date());
	}

	// 엑세스 토큰 생성
	public String createAccessToken(Long userId, String mobilenumber, String role) {
		return Jwts.builder()
			.claim("userId", userId)
			.claim("mobilenumber", mobilenumber)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis())) // 현재 발행시간
			.expiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))   // 소멸시간
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();

	}

	public String createAccessToken(String mobilenumber, String role) {
		return Jwts.builder()

			.claim("mobilenumber", mobilenumber)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis())) // 현재 발행시간
			.expiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))   // 소멸시간
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();

	}

	// 리프레시 토큰 생성
	public String createRefreshToken(Long userId, String mobilenumber) {
		return Jwts.builder()
			.claim("usderId", String.valueOf(userId))
			.setSubject(mobilenumber)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_EXPIRATION_TIME))
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();

	}

	public String createRefreshToken(String mobilenumber) {
		return Jwts.builder()
			.setSubject(mobilenumber)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_EXPIRATION_TIME))
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();

	}

	public Jws<Claims> parseToken(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		} catch (Exception e) {
			throw new RuntimeException("토큰이 유효하지 않습니다", e);
		}
	}

	public Date getTokenExpiration(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration();
	}

	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.setSigningKey(secretKey)
				.build()
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
			.build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

}