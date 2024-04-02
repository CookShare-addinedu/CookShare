package com.example.foodshare.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.foodshare.domain.ChatRoom;
import com.example.foodshare.dto.ChatMessageDTO;
import com.example.foodshare.service.ChatService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate template;
	private final ChatService chatService;

	@MessageMapping("/chat.sendMessage")  // 클라이언트에서 "/app/chat.sendMessage"로 메시지를 보낼 때 매핑되는 핸들러
	public void message(@Payload ChatMessageDTO message) {
		ChatRoom chatRoom = chatService.addMessageToChatRoom(message.getChatRoomId(), message.getSender(),
			message.getContent());

		System.out.println(message);
		template.convertAndSend("/topic/public", message);
	}
}

