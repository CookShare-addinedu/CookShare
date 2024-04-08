package com.example.foodshare.service;

import com.example.foodshare.domain.User;
import com.example.foodshare.dto.EnrollUser;
import com.example.foodshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    @Override
    public User registerUser(User user) {
        // 사용자의 비밀번호를 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll(); // 모든 사용자 조회
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.findByNickName(nickname).isPresent();
    }
    /*비번 암호화*/
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    public void userEnroll(EnrollUser enrollUser) {
//        User data = new User();  /*DB에 있는 엔티티르르 엔롤로*/
//        data.setMobileNumber(enrollUser.getMobileNumber());
//        data.setPassword(bCryptPasswordEncoder.encode(enrollUser.getPassword()));
//        /*data.setRole("ROLE_ADMIN");*/
//        data.setRole("ROLE_USER");
//
//        userRepository.save(data);
//    }
}

