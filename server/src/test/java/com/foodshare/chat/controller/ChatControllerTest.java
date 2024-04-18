package com.foodshare.chat.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.foodshare.chat.dto.ChatMessageDto;
import com.foodshare.chat.service.ChatMessageService;

@WebMvcTest(ChatMessageController.class)
public class ChatControllerTest {

	@Autowired
	private ChatMessageController chatController;

	@MockBean
	private ChatMessageService chatMessageService;

	@MockBean
	private SimpMessagingTemplate messagingTemplate;

	@Test

	public void testMessage() {
		String chatRoomId = "testRoomId";
		ChatMessageDto message = new ChatMessageDto();
		message.setChatRoomId(chatRoomId);
		message.setSender("testUser");
		message.setContent("Hello, World!");

		// 실행
		chatController.message(chatRoomId, message);//message라는 컨트롤러

		verify(chatMessageService, times(1)).addMessageToChatRoom(chatRoomId, message.getSender(),
			message.getContent());
		//times(n)는 verify 메서드와 함께 사용되어, 검증하고자 하는 메서드가 정확히 n번 호출되었는지를 검사
		verify(messagingTemplate, times(1)).convertAndSend(String.format("/topic/chat/room/%s", chatRoomId), message);
	}
}
