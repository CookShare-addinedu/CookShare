package com.example.foodshare.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.foodshare.chat.dto.ChatMessageDTO;
import com.example.foodshare.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate template;
	private final ChatService chatService;

	@MessageMapping("/chat.sendMessage")
	public void message(@Payload ChatMessageDTO message) {
		log.info("채팅 시작");
		log.debug(message.toString());

		chatService.addMessageToChatRoom(message.getChatRoomId(), message.getSender(), message.getContent());

		template.convertAndSend("/topic/public", message);
	}

}