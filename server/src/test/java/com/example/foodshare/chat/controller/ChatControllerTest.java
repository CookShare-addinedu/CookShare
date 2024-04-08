package com.example.foodshare.chat.controller;

import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.example.foodshare.chat.dto.ChatMessageDTO;
import com.example.foodshare.chat.service.ChatMessageService;
import com.example.foodshare.domain.ChatRoom;

@WebMvcTest(MessageChatController.class)
public class ChatControllerTest {

	@Autowired
	private MessageChatController chatController;

	@MockBean
	private ChatMessageService chatMessageService;

	@MockBean
	private SimpMessagingTemplate messagingTemplate;

	@Test

	public void testMessage() {
		ChatMessageDTO message = new ChatMessageDTO();
		message.setChatRoomId("testRoomId");
		message.setSender("testUser");
		message.setContent("Hello, World!");

		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setId(message.getChatRoomId());
		chatRoom.addMessage(new ChatRoom.ChatMessages(message.getSender(), message.getContent(), new Date()));

	//	when(chatMessageService.addMessageToChatRoom(anyString(), anyString(), anyString())).thenReturn(chatRoom);

		//chatController.message(message);

		verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/public"), any(ChatMessageDTO.class));
	}
}
