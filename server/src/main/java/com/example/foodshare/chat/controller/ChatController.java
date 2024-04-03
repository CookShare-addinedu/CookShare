package com.example.foodshare.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.foodshare.chat.dto.ChatMessageDTO;
import com.example.foodshare.chat.service.ChatService;
import com.example.foodshare.domain.ChatRoom;

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
		ChatRoom chatRoom = chatService.addMessageToChatRoom(message.getChatRoomId(), message.getSender(),
			message.getContent());

		System.out.println(message);
		template.convertAndSend("/topic/public", message);
	}

}