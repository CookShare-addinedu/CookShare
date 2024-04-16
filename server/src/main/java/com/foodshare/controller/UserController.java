package com.foodshare.controller;

import com.foodshare.domain.User;
import com.foodshare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (userService.findByMobileNumber(user.getMobileNumber()).isPresent()) {
            // 이미 존재하는 모바일 번호인 경우, 충돌을 나타내는 HTTP 상태 코드 반환
            return ResponseEntity.badRequest().build();
        }
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = userService.checkNicknameDuplicate(nickname);
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}
