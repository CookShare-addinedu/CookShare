package com.foodshare.security.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodshare.domain.User;
import com.foodshare.security.jwt.JwtUtil;
import com.foodshare.security.jwt.SecurityConstants;
import com.foodshare.security.jwt.AuthenticationRequest;
import com.foodshare.security.service.RedisService;
import com.foodshare.security.service.UserService;
import com.foodshare.security.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.foodshare.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.foodshare.security.jwt.SecurityConstants.REFRESH_EXPIRATION_TIME;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    // 로그인 API는 JWT 생성 및 발급 로직을 포함합니다.
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
//        String mobileNumber = request.getMobileNumber();
//        String password = request.getPassword();
//
//        Optional<User> userOptional = userService.findByMobileNumber(mobileNumber);
//        if (!userOptional.isPresent() || !loginService.checkPassword(password, userOptional.get().getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("번호 or 비밀번호가 일치하지 않음 ");
//        }
//
//        User user = userOptional.get();
//        String accessToken = jwtUtil.createAccessToken(mobileNumber, "ROLE_USER", SecurityConstants.EXPIRATION_TIME);
//        String refreshToken = jwtUtil.createRefreshToken(mobileNumber, SecurityConstants.REFRESH_EXPIRATION_TIME);
//        long accessTokenExpirationMs = System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME;
//        long refreshTokenExpirationMs = System.currentTimeMillis() + SecurityConstants.REFRESH_EXPIRATION_TIME;
//
//
//        Map<String, Object> tokenDetails = new HashMap<>();
//        tokenDetails.put("accessToken", accessToken);
//        tokenDetails.put("refreshToken", refreshToken);
//        tokenDetails.put("accessTokenExpiresIn", new Date(accessTokenExpirationMs));
//        tokenDetails.put("refreshTokenExpiresIn", new Date(refreshTokenExpirationMs));
//
//
//        // JWT를 헤더와 본문에 추가하여 반환
//        return ResponseEntity.ok()
//                .header(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + accessToken)
//                .header(SecurityConstants.REFRESH_TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + refreshToken)
//                .body("엑세스 토큰 , 리프레쉬 토큰 모두 생성 " + tokenDetails);
//
//    }


//    @GetMapping("/info")
//    public ResponseEntity<?> userInfo(@RequestHeader(name = "Authorization") String header) {
//        String token = header.replace(SecurityConstants.TOKEN_PREFIX, "").trim();
//        try {
//            if (!jwtUtil.isExpired(token)) {
//                Date expiration = jwtUtil.getTokenExpiration(token);
//                long expiresIn = expiration.getTime() - System.currentTimeMillis(); // 남은 시간 밀리초
//                long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(expiresIn); // 밀리초를 분으로 변환
//
//                String mobileNumber = jwtUtil.getMobileNumber(token);
//                String responseMessage = String.format("Mobile Number: %s. 토큰이 유효합니다. 엑세스 토큰 남은 시간: %d 분", mobileNumber, minutesLeft);
//                return ResponseEntity.ok(responseMessage);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("만료된 토큰입니다.");
//            }
//        } catch (ExpiredJwtException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("만료된 토큰입니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
//        }
//    }

    // 사용자의 자세한 페이지 정보를 반환하는 API
    // LoginController.java

//    @GetMapping("/mypage")
//    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String header) {
//        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "").trim();
//
//        if (jwt.isEmpty()) {
//            log.warn("JWT is empty after removing Bearer prefix.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT found in request header.");
//        }
//
//        Jws<Claims> parsedToken;
//        try {
//            parsedToken = jwtUtil.parseToken(jwt);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다");
//        }
//
//        String mobileNumber = parsedToken.getBody().getSubject();
//        User user = userService.findByMobileNumber(mobileNumber).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // 반환 데이터에 필요한 사용자 정보만 포함
//        return ResponseEntity.ok(user);
//    }



    @GetMapping("/checkNickName")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = loginService.checkNicknameDuplicate(nickname);
        if(isDuplicate) {
            // 닉네임이 중복되었을 경우 JSON 객체로 중복 여부 전송
            return ResponseEntity.ok(Map.of("isUnique", false, "message", "닉네임이 중복됩니다."));
        } else {
            // 닉네임이 중복되지 않았을 경우
            return ResponseEntity.ok(Map.of("isUnique", true, "message", "사용할 수 있는 닉네임입니다."));
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@CookieValue(name = "Refresh-Token") String encodedRefreshToken, HttpServletResponse response) {
        System.out.println("엑세스토큰 새로 발급하기 ");
        try {
            System.out.println("디코딩 진입");
            // "Bearer%20" 접두사와 함께 인코딩된 토큰을 디코딩
            String refreshTokenWithBearer = URLDecoder.decode(encodedRefreshToken, StandardCharsets.UTF_8.toString());
            System.out.println("디코딩 후: " + refreshTokenWithBearer);
            String refreshToken = refreshTokenWithBearer.replace("Bearer ", "");
            System.out.println("Bearer 접두사 제거 후: " + refreshToken);
            // 리프레시 토큰 검증
            if (!jwtUtil.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 유효하지 않습니다.");
            }

            // 사용자 정보 및 새 토큰 생성
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            String role = jwtUtil.getRole(refreshToken);
            String newAccessToken = jwtUtil.createAccessToken(username, role);
            String newRefreshToken = jwtUtil.createRefreshToken(username);
            long accessTokenExpirationMs = System.currentTimeMillis() + EXPIRATION_TIME;
            long refreshTokenExpirationMs = System.currentTimeMillis() + REFRESH_EXPIRATION_TIME;


            // 쿠키에 새 리프레시 토큰 설정 (인코딩)
            String encodedNewRefreshToken = URLEncoder.encode("Bearer " + newRefreshToken, StandardCharsets.UTF_8.toString());
            Cookie refreshCookie = new Cookie("Refresh-Token", encodedNewRefreshToken);

            // 테스트용 :false ,  원래는 : true
            refreshCookie.setHttpOnly(false);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            // 서버 : Redis에 refreshToken 저장
            System.out.println("토큰발급한 번호: " + username);
            // Redis에 다시 저장
            redisService.saveToken(username, newRefreshToken, REFRESH_EXPIRATION_TIME);

            // 헤더에 새 토큰으로 변경
            response.addHeader( "Authorization", "Bearer " + newAccessToken);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // ISO 8601 형식

            Map<String, Object> responseData  = new HashMap<>();
            responseData .put("새로운 엑세스토큰(accessToken)", newAccessToken);
            responseData .put("새로운 리프레시토큰(refreshToken)", newRefreshToken);
            responseData .put("새로운 엑세스토큰유효기간(accessTokenExpiresIn)", dateFormat.format(new Date(accessTokenExpirationMs)));
            responseData .put("새로운 리프레시토큰유효기간(refreshTokenExpiresIn)", dateFormat.format(new Date(refreshTokenExpirationMs)));
            responseData .put("role", role);

            // 응답 반환
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .body("새 토큰(엑세스,리프레쉬)이 생성되었습니다." + responseData );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token processing error: " + e.getMessage());
        }
    }

}
