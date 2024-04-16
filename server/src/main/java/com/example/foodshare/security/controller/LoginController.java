package com.example.foodshare.security.controller;

import com.example.foodshare.domain.User;
import com.example.foodshare.security.jwt.AuthenticationRequest;
import com.example.foodshare.security.jwt.JwtProp;
import com.example.foodshare.security.jwt.SecurityConstants;
import com.example.foodshare.security.service.LoginServiceImpl;
import com.example.foodshare.security.service.UserService;
import com.example.foodshare.security.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
    private LoginService loginService;

    @Autowired
    private JwtProp jwtProp;

    // 로그인 API는 JWT 생성 및 발급 로직을 포함합니다.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        String mobileNumber = request.getMobileNumber();
        String password = request.getPassword();

        Optional<User> userOptional = userService.findByMobileNumber(mobileNumber);
        if (!userOptional.isPresent() || !loginService.checkPassword(password, userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid mobile number or password.");
        }

        User user = userOptional.get();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        byte[] signingKey = jwtProp.getSecretKey().getBytes();

        String jwt = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setSubject(mobileNumber)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5)) // 5일 후 만료
                .claim("rol", roles)
                .compact();
        log.info("JWT: " + jwt);

        // JWT를 헤더와 본문에 추가하여 반환
        return ResponseEntity.ok().header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + jwt).body(jwt);
    }

    // 인증된 사용자의 정보를 반환하는 API
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@RequestHeader(name = "Authorization") String header) {
        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "").trim();

        byte[] signingKey = jwtProp.getSecretKey().getBytes();

        Jws<Claims> parsedToken;
        try {
            parsedToken = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKey))
                    .build()
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        String mobileNumber = parsedToken.getBody().getSubject();
        List<String> roles = (List<String>) parsedToken.getBody().get("rol");

        log.info("Mobile Number: " + mobileNumber);
        log.info("Roles: " + roles.toString());

        return ResponseEntity.ok(parsedToken.getBody());
    }

    // 사용자의 자세한 페이지 정보를 반환하는 API
    @GetMapping("/mypage")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String header) {
        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "").trim();

        if (jwt.isEmpty()) {
            log.warn("JWT is empty after removing Bearer prefix.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT found in request header.");
        }

        byte[] signingKey = jwtProp.getSecretKey().getBytes();
        Jws<Claims> parsedToken;
        try {
            parsedToken = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKey))
                    .build()
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다");
        }

        String mobileNumber = parsedToken.getBody().getSubject();
        User user = userService.findByMobileNumber(mobileNumber).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 반환 데이터에 필요한 사용자 정보만 포함
        return ResponseEntity.ok(user);
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = loginService.checkNicknameDuplicate(nickname);
        if(isDuplicate) {
            // 닉네임이 중복되었을 경우
            //return ResponseEntity.badRequest().body("닉네임이 중복됩니다.");
            return ResponseEntity.badRequest().body("");
        } else {
            // 닉네임이 중복되지 않았을 경우
            //return ResponseEntity.ok("사용할 수 있는 닉네임입니다.");
            return ResponseEntity.ok("사용할 수 있는 닉네임입니다.");
        }
    }

}
