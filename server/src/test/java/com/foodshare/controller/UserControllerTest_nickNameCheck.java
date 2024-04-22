package com.foodshare.controller;

import com.foodshare.security.controller.UserController;
import com.foodshare.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest_nickNameCheck {

    @Autowired
    private MockMvc mockMvc;
    private String nickName;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        nickName = "고래밥";
    }

    @Test
    void checkNickname_WhenNicknameIsNotDuplicate_ShouldReturnOk() throws Exception {
        String nickname = "testNickname";
        when(userService.checkNicknameDuplicate(nickname)).thenReturn(false);

        mockMvc.perform(get("/api/user/checkNickname")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("사용할 수 있는 닉네임입니다."));
    }

    @Test
    void checkNickname_WhenNicknameIsDuplicate_ShouldReturnBadRequest() throws Exception {
        String nickname = "duplicateNickname";
        when(userService.checkNicknameDuplicate(nickname)).thenReturn(true);

        mockMvc.perform(get("/api/user/checkNickname")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}
