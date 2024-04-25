package com.foodshare.security.service;

public interface LoginService {
    boolean checkPassword(String rawPassword, String encodedPassword);

    boolean checkNicknameDuplicate(String nickname);
}
