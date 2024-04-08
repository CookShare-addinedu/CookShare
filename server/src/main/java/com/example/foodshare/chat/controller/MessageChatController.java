package com.example.foodshare.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.foodshare.chat.dto.ChatMessageDTO;
import com.example.foodshare.chat.service.ChatMessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageChatController {

	private final SimpMessagingTemplate template;
	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat.room/{chatRoomId}/sendMessage")
	public void message(@DestinationVariable String chatRoomId, @Payload ChatMessageDTO message) {
		log.info("채팅 시작");
		log.debug(message.toString());

		chatMessageService.addMessageToChatRoom(message.getChatRoomId(), message.getSender(), message.getContent());

		template.convertAndSend(String.format("/topic/chat/room/%s", chatRoomId), message);

	}

}