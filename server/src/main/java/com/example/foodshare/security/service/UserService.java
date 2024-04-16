package com.example.foodshare.security.service;

import com.example.foodshare.domain.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<User> findByMobileNumber(String mobileNumber);
    User registerUser(User user);
    User updateUserByMobileNumber(User user);
    List<User> findAllUsers();

    void deleteUserByMobileNumber(String mobileNumber);

}