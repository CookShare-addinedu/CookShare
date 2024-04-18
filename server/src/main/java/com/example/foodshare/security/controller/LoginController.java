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
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtProp jwtProp;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        String mobileNumber = request.getTel();
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

        return ResponseEntity.ok().header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + jwt).body(jwt);
    }


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


        return ResponseEntity.ok(user);
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = loginService.checkNicknameDuplicate(nickname);
        if(isDuplicate) {
            return ResponseEntity.badRequest().body("");
        } else {
            return ResponseEntity.ok("사용할 수 있는 닉네임입니다.");
        }
    }

}
