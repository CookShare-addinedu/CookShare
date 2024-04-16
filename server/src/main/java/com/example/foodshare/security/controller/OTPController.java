package com.example.foodshare.security.controller;


import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.ResponseEntity;
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
       this.messageService = NurigoApp.INSTANCE.initialize("NCSIFI4ROLXOUATB", "S0IX1HZM9VHKKCNR2OW80BQNNHWPS9I1", "https://api.coolsms.co.kr");

    }


    @PostMapping("/memberPhoneCheck")
    public ResponseEntity<Map<String, Object>> memberPhoneCheck(@RequestBody Map<String, Object> payload) {
        String toPhoneNumber = (String) payload.get("to");

        String checkNum = generateCheckNum();

        Message message = new Message();
        message.setFrom("01025404366");
        message.setTo(toPhoneNumber);
        message.setText("쿡쉐어(CookShare) 인증번호 입니다: " + checkNum);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return ResponseEntity.ok(Map.of("checkNum", checkNum));
    }

    // 인증번호 생성 메서드
    private String generateCheckNum() {
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }


}

