package com.example.foodshare.chat.service;

import static org.mockito.AdditionalAnswers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
	@DisplayName("채팅방생성")
	public void testCreateChatRoom() {
		String userA = "UserA";
		String userB = "UserB";

		chatService.createChatRoom(userA, userB);

		verify(chatRepository).save(any(ChatRoom.class));
		System.out.println("채팅방 생성 성공: " + userA + ", " + userB);
	}

	@Test
	@DisplayName("메시지 추가 테스트")
	public void testAddMessageToChatRoom() {

		String userA = "UserA";
		String userB = "UserB";
		String chatRoomId = userA + "_" + userB;
		String sender = userA;
		String messageContent = "Hello, World!";


		ChatRoom mockChatRoom = new ChatRoom();
		mockChatRoom.setId(chatRoomId);


		when(chatRepository.findById(chatRoomId)).thenReturn(Optional.of(mockChatRoom));
		when(chatRepository.save(any(ChatRoom.class))).thenReturn(mockChatRoom);


		chatService.addMessageToChatRoom(chatRoomId, sender, messageContent);

		// 결과 검증
		verify(chatRepository).findById(chatRoomId);
		verify(chatRepository).save(mockChatRoom);
		System.out.println("메시지 추가 성공: " + messageContent + " in " + chatRoomId);
	}

}