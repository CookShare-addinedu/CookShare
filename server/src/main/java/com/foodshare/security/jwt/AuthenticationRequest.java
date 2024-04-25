package com.foodshare.security.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private  String password;
    private  String tel;
}

