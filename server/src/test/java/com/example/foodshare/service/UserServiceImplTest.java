package com.example.foodshare.service;

import com.example.foodshare.domain.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

//    @Before()
    public void setUp() {
        User user = new User();
        user.setMobileNumber("010-1111-2222");
        user.setUserId(1L);
        user.setPassword("1111");

    }
    @Test
    void findByMobileNumber() {

    }
}