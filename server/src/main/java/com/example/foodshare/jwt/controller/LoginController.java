package com.example.foodshare.jwt.controller;

import com.example.foodshare.jwt.SecurityConstants;
import com.example.foodshare.jwt.domain.AuthenticationRequest;
import com.example.foodshare.jwt.prop.JwtProp;
import com.example.foodshare.domain.User;
import com.example.foodshare.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/user")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtProp jwtProp;

    //  /login
    //  - mobileNumber
    //  - password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {

        String mobileNumber = request.getMobileNumber();
        String password = request.getPassword();

        Optional<User> userOptional = userService.findByMobileNumber(mobileNumber);
        if (!userOptional.isPresent() || !userService.checkPassword(password, userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid mobile number or password.");
        }

        //log.info("mobileNumber : " + mobileNumber);
        //log.info("password : " + password);

        // 사용자 권한
        User user = userOptional.get();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        // 시크릿키 -> 바이트
        byte[] signingKey = jwtProp.getSecretKey().getBytes();

        // 토큰 생성
        String jwt = Jwts.builder()
                //.signWith( 시크릿키, 알고리즘)
                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)   // 시그니처 사용할 비밀키, 알고리즘 설정
                .header()                                      // 헤더 설정
                .add("typ", SecurityConstants.TOKEN_TYPE) // type: JWT
                .and()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5)) // 토큰만료시간설정 (5일)
                .claim("uid", mobileNumber)            // PAYLOAD - uid : mobilenumber (사용자 아이디)
                .claim("rol", roles)                           // PAYLOAD - rol : [Role_USER, Role_ADMIN] (권한정보)
                .compact();                                       // 토큰 생성
        log.info("jwt : " + jwt);

        return new ResponseEntity<String>(jwt, HttpStatus.OK);
    }

    // 토큰 해석 (권한 설정해서 엔드포인트 어느정도 까지 허용할지, 유저, 관리자)
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@RequestHeader(name = "Authorization") String header) {

        log.info("======header=======");
        log.info("Authorization : " + header);

        // Authorization : Bearer ${jwt}
        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "").trim();
        //String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");

        // 시크릿 키
        byte[] signingKey = jwtProp.getSecretKey().getBytes();

        Jws<Claims> parsedToken = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(signingKey))
                .build()
                .parseSignedClaims(jwt);  // 암호화된 데이터를 다시 변환 (디코드 작업)

        log.info("parsedToken : " + parsedToken);
        // uid : user
        String mobileNumber = parsedToken.getPayload().get("uid").toString();
        log.info("mobileNumber : " + mobileNumber);

        // rol : [ROLE_USER, ROLE_ADMIN]
        Claims claims = parsedToken.getPayload();
        Object roles = claims.get("rol");
        log.info("roles : " + roles);
        return new ResponseEntity<String>(parsedToken.toString(), HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String header) {
        // Authorization 헤더에서 토큰 추출
        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "").trim();

        // 토큰 검증 및 클레임 추출
        byte[] signingKey = jwtProp.getSecretKey().getBytes();
        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(signingKey))
                .build()
                .parseClaimsJws(jwt);
        String mobileNumber = parsedToken.getBody().get("uid", String.class);

        // 데이터베이스에서 사용자 정보 조회
        User user = userService.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 필요한 정보만 응답으로 전송
        return ResponseEntity.ok(user);
    }


}
