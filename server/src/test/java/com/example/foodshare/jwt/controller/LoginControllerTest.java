package com.example.foodshare.jwt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // CSRF 필터를 비활성화하고 MockMvc를 자동 구성
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String mobileNumber;
    private String password;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 모바일 번호와 비밀번호를 설정합니다.
        mobileNumber = "1111";
        password = "1111";
    }

    @Test
    @WithMockUser // 테스트용 목 사용자 권한을 설정합니다.
    void login_Success() throws Exception {
        // 성공적인 로그인 시나리오를 테스트합니다.
        ResultActions result = mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"mobileNumber\":\"" + mobileNumber + "\", \"password\":\"" + password + "\"}"));

        result.andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyString())));
    }

    @Test
    @WithMockUser // 테스트용 목 사용자 권한을 설정합니다.
    void login_Failure() throws Exception {
        // 실패한 로그인 시나리오를 테스트합니다. 여기서는 목 객체 설정이 필요합니다.
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mobileNumber\":\"wrongnumber\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }
}









