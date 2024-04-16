package com.foodshare.controller;


import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageStatusType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import net.nurigo.sdk.NurigoApp;

import java.util.Map;
import java.util.Random;

@RestController
public class OTPController {
    final DefaultMessageService messageService;

    public OTPController() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize("NCSIFI4ROLXOUATB", "S0IX1HZM9VHKKCNR2OW80BQNNHWPS9I1", "https://api.coolsms.co.kr");

    }

    /**
     * 단일 메시지 발송 예제
     */
    @PostMapping("/memberPhoneCheck")
    public ResponseEntity<Map<String, Object>> memberPhoneCheck(@RequestBody Map<String, Object> payload) {
        String toPhoneNumber = (String) payload.get("to"); // to로 전화번호 받음

        // 인증번호 생성
        String checkNum = generateCheckNum();

        Message message = new Message();
        message.setFrom("01025404366"); // 발신번호
        message.setTo(toPhoneNumber);  // 수신번호
        message.setText("FoodShare 인증번호 입니다: " + checkNum); // 인증번호를 포함한 메시지 텍스트

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        // 클라이언트에 인증번호를 포함하여 응답 전송
        return ResponseEntity.ok(Map.of("checkNum", checkNum));
    }

    // 인증번호 생성 메서드
    private String generateCheckNum() {
        Random random = new Random();
        int number = random.nextInt(999999); // 0 ~ 999999 사이의 난수 생성
        return String.format("%06d", number); // 6자리 숫자 형태로 포맷
    }


}

