package com.foodshare.service;

import com.foodshare.domain.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<User> findByMobileNumber(String mobileNumber);
    User registerUser(User user);

    List<User> findAllUsers();

    boolean checkPassword(String rawPassword, String encodedPassword);

    boolean checkNicknameDuplicate(String nickname);
}