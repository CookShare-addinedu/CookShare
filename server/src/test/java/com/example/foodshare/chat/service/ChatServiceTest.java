package com.example.foodshare.chat.service;

import static org.mockito.AdditionalAnswers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.foodshare.chat.repository.ChatRepository;
import com.example.foodshare.domain.ChatRoom;

@SpringBootTest
public class ChatServiceTest {

	@Autowired
	private ChatService chatService;

	@MockBean
	private ChatRepository chatRepository;

	@Test
	@DisplayName("채팅방 생성")
	public void createChatRoomTest() {

		String userA = "UserA";
		String userB = "UserB";
		String chatRoomId = userA + "_" + userB;

		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setId(chatRoomId);
		chatRoom.setFirstUser(userA);
		chatRoom.setSecondUser(userB);
		chatRoom.setCreatedAt(new Date());

		when(chatRepository.save(any(ChatRoom.class)))
			.thenReturn(chatRoom);

		ChatRoom createdChatRoom = chatService.createChatRoom(userA, userB);

		assertNotNull(createdChatRoom);
		assertEquals(chatRoomId, createdChatRoom.getId());
		verify(chatRepository).save(any(ChatRoom.class));

		System.out.println("채팅방 생성 성공: " + createdChatRoom.getId());
	}

	@Test
	@DisplayName("채팅방에 메시지 추가 ")
	public void addMessageToChatRoom() {

		String chatRoomId = "UserA_UserB";
		String sender = "UserA";
		String messageContent = "Hello, World!";
		Date now = new Date();

		ChatRoom expectedChatRoom = new ChatRoom();
		expectedChatRoom.setId(chatRoomId);
		expectedChatRoom.setCreatedAt(now);
		expectedChatRoom.setContent(new ArrayList<>());

		when(chatRepository.findById(chatRoomId))
			.thenReturn(Optional.of(expectedChatRoom));

		when(chatRepository.save(any(ChatRoom.class)))
			.thenAnswer(i -> i.getArgument(0));

		chatService.addMessageToChatRoom(chatRoomId, sender, messageContent);

		verify(chatRepository).findById(chatRoomId);
		verify(chatRepository).save(expectedChatRoom);

		assertFalse(expectedChatRoom.getContent().isEmpty(), "메시지는 비어있으면 안됨 .");
		assertEquals(messageContent, expectedChatRoom.getContent().get(0).getContent(), "메시지 내용은 일치해야함 .");
	}
}









