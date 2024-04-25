package com.foodshare.controller;

import com.foodshare.security.controller.OTPController;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OTPController.class)
public class OTPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultMessageService defaultMessageService; // DefaultMessageService의 모킹

    @Autowired
    @InjectMocks
    private OTPController otpController;

    private ObjectMapper objectMapper = new ObjectMapper();

    public enum MessageType {
        SMS, LMS, MMS
    }
    @BeforeEach
    void setUp() {
        // SingleMessageSentResponse 객체를 생성하기 위한 모의 매개변수 값
        String messageId = "mockMessageId";
        String groupId = "mockGroupId";
        String to = "01012345678"; // 예제로 사용될 전화번호
        String from = "senderPhoneNumber";
        MessageType type = MessageType.SMS; // 메시지 타입을 MessageType 열거형으로 지정
        String statusCode = "200";
        String statusMessage = "Success";
        String customField1 = "custom1";
        String customField2 = "custom2";
        String customField3 = "custom3";

        // 모의 매개변수 값을 사용하여 SingleMessageSentResponse 객체 생성
        SingleMessageSentResponse mockResponse = new SingleMessageSentResponse(
                messageId, groupId, to, type , from, statusCode, statusMessage, customField1, customField2, customField3
        );

        // defaultMessageService의 sendOne 메서드를 호출할 때 mockResponse를 반환하도록 설정
        when(defaultMessageService.sendOne(any())).thenReturn(mockResponse);
    }


    @Test
    public void memberPhoneCheckTest() throws Exception {
        String toPhoneNumber = "01012345678";
        String requestBody = objectMapper.writeValueAsString(Map.of("to", toPhoneNumber));

        mockMvc.perform(post("/memberPhoneCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{'checkNum':'expectedCheckNumValue'}", false)); // 'expectedCheckNumValue'를 실제 예상되는 인증번호 값으로 대체해야 합니다.
    }
}
