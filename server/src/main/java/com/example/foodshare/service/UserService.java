package com.example.foodshare.service;

import com.example.foodshare.domain.User;
import com.example.foodshare.dto.EnrollUser;
import com.example.foodshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    /*비번 암호화*/
     @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public static void userEnroll(EnrollUser enrollUser) {
        User data = new User();  /*DB에 있는 엔티티르르 엔롤로*/
        data.setUsername(enrollUser.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(enrollUser.getPassword()));
        /*data.setRole("ROLE_ADMIN");*/
        /*data.setRole("ROLE_USER");*/

        UserRepository.save(data);
    }
}

