package com.example.foodshare.chat.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.foodshare.chat.repository.ChatRepository;
import com.example.foodshare.domain.ChatRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

	@InjectMocks
	private ChatService chatService;

	@Mock
	private ChatRepository chatRepository;

	private String chatRoomId;
	private ChatRoom expectedChatRoom;
	private String userA;
	private String userB;

	@BeforeEach
	void setUp() {
		userA = "User1";
		userB = "User2";
		chatRoomId = userA + "_" + userB;
		expectedChatRoom = ChatRoom.builder()
			.id(chatRoomId)
			.firstUser(userA)
			.secondUser(userB)
			.createdAt(new Date())
			.content(new ArrayList<>())
			.build();
	}

	@Test
	@DisplayName("채팅방 생성")
	public void createChatRoomTest() {
		when(chatRepository.save(any()))
			.thenReturn(expectedChatRoom);

		// when
		ChatRoom result = chatService.createChatRoom(userA, userB);

		// then
		assertThat(result.getId()).isEqualTo(chatRoomId);
		log.info("채팅방이 생성되었습니다.");
	}

	@Test
	@DisplayName("채팅방에 메시지 추가")
	void addMessageToChatRoomTest() {
		// given(준비)
		String sender = "UserA";
		String messageContent = "Hello, World!";

		when(chatRepository.findById(chatRoomId))  //findById 메소드가 chatRoomId와 함께 호출될 때를 가정합
			.thenReturn(Optional.of(expectedChatRoom)); //위의 가정에 따라 호출될 때, expectedChatRoom을 포함하는 Optional 객체를 반환

		// when (실행) 하면
		chatService.addMessageToChatRoom(chatRoomId, sender, messageContent);

		// then (검증) 어떠한 결과가 나와야함
		Optional<ChatRoom> savedChatRoom = chatRepository.findById(chatRoomId);
		assertTrue(savedChatRoom.isPresent(), "채팅방이 저장되어있지 않음.");
		log.info("채팅방이 존재.");

		//// 해당 메소드가 호출되었는지 검증
		verify(chatRepository).save(any(ChatRoom.class));
		log.info("메시지 저장.");

		ChatRoom savedRoom = savedChatRoom.get();
		assertFalse(savedRoom.getContent().isEmpty(), "메시지는 비어있으면 안됨.");

		ChatRoom.ChatMessages lastMessage = savedRoom.getContent().get(0);
		assertThat(lastMessage.getSender()).isEqualTo(sender);
		assertThat(lastMessage.getContent()).isEqualTo(messageContent);
	}

	@Test
	@DisplayName("현재 사용자가 속한 채팅방 목록 조회")
	void getChatRoomListForCurrentUserTest() {
		// given
		List<ChatRoom> expectedChatRooms = Arrays.asList(
			ChatRoom.builder().id("User1_User2").build(),
			ChatRoom.builder().id("User1_User3").build()
		);

		// 현재 사용자와 연관된 채팅방 목록을 반환하도록 설정
		when(chatRepository.findChatRoomsByFirstUserOrSecondUser(userA, userA))
			.thenReturn(expectedChatRooms);

		// when
		List<ChatRoom> result = chatService.getChatRoomListForUser(userA);
		log.info("사용자의 채팅목록 불러오기 성공.");
		// then
		assertThat(result).containsExactlyElementsOf(expectedChatRooms);
	}

}






