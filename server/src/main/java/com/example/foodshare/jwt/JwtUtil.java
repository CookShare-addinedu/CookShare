package com.example.foodshare.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey secretKey;

    //application.properties에 정의한 SecretKey를 불러 온다
    public JwtUtil(@Value("${spring.jwt.secret}") String secret){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 검증
    public  String getUsername(String token){
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload()
                .get("username", String.class);
        //유저네임 검증하고
    }

    public String getRole(String token){
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("role", String.class);
        // role도 검증하고
    }

    //시간을 넣어서 만기가 되었는지 확인
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());

    }

    //실제로 토큰 생성
    public  String createJwt(String username,
                             Long role,
                             Long expiredMs){
        return Jwts.builder() //claim에는 어드레스 넣어주던가 넣고 싶은것 넣어도 된다.
                //토큰 발행시간, 소멸시간 넣어줘야 한다.
                .claim("username", username)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis())) //현재 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs )) //소멸시간
                .signWith(secretKey) //시그니처 , 시크릿키를 넣어줌
                .compact();

        //반드시 signwith, issuedAt, expiration 모두 필요
    }

}


