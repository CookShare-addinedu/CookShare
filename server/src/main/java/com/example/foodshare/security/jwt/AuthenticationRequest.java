package com.example.foodshare.security.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private  String mobileNumber;
    private  String password;
    //위에 2개를 파라미터 바인딩

}

