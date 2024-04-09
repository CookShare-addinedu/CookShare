package com.example.foodshare.chat.service;

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

import com.example.foodshare.chat.repository.ChatMessageRepository;
import com.example.foodshare.chat.repository.ChatRoomRepository;
import com.example.foodshare.domain.ChatRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

	@InjectMocks
	private ChatMessageService chatMessageService;

	@InjectMocks
	private ChatRoomService chatRoomService;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	private String chatRoomId;
	private ChatRoom expectedChatRoom;
	private String userA;
	private String userB;

	@BeforeEach
	void setUp() {
		userA = "userA";
		userB = "userB";
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
	public void testCreateChatRoomTest() {

		when(chatRoomRepository.save(any(ChatRoom.class)))
			.thenReturn(expectedChatRoom);
		// when
		ChatRoom result = chatRoomService.createChatRoom(userA, userB);

		// then
		assertThat(result.getId()).isEqualTo(chatRoomId);
		log.info("채팅방이 생성되었습니다.");
	}

	@Test
	@DisplayName("현재 사용자가 속한 채팅방 목록 조회")
	void testFindRoomsByUserId() {
		// given
		List<ChatRoom> expectedChatRooms = Arrays.asList(
			ChatRoom.builder()
				.id("User1_User2")
				.build(),
			ChatRoom.builder()
				.id("User1_User3")
				.build()
		);

		// 현재 사용자와 연관된 채팅방 목록을 반환하도록 설정
		when(chatRoomRepository.findByFirstUserOrSecondUser(userA, userA))
			.thenReturn(expectedChatRooms);

		// when
		List<ChatRoom> result = chatRoomService.findRoomsByUserId(userA);

		// then
		assertThat(result).containsExactlyElementsOf(expectedChatRooms);
		log.info("사용자의 채팅목록 불러오기 성공.");
		//, 주어진 컬렉션이 특정 요소들을 정확히, 그리고 순서대로 포함하고 있는지 검사ㅇ
	}

	@Test
	@DisplayName("사용자가 방에 속해 있을 때")
	void testFindRoomByIdAndUserId_UserBelongsToRoom() {
		// 준비
		String roomId = "room1";
		String userId = "user1";
		ChatRoom expectedRoom = new ChatRoom();
		expectedRoom.setFirstUser("user1");
		expectedRoom.setSecondUser("user2");
		when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(expectedRoom));

		// 실행
		Optional<ChatRoom> result = chatRoomService.findRoomByIdAndUserId(roomId, userId);

		// 검증
		assertThat(result).contains(expectedRoom);
		log.info("User {} 사용자가 채팅방에 존재", userId);

	}

	@Test
	@DisplayName("사용자가 방에 속하지 않았을 때")
	void testFindRoomByIdAndUserId_UserDoesNotBelongToRoom() {
		// 준비
		String roomId = "room1";
		String userId = "user2";
		ChatRoom room = new ChatRoom();
		when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(room));

		// 실행
		Optional<ChatRoom> result = chatRoomService.findRoomByIdAndUserId(roomId, userId);

		// 검증
		assertThat(result).isEmpty();
		log.info("User {} 사용자가 채팅방에 존재하지 않음", userId);
	}

	@Test
	@DisplayName("방을 찾을 수 없을 때")
	void testFindRoomByIdAndUserId_RoomDoesNotExist() {
		// 준비
		String roomId = "nonExistentRoom";
		String userId = "user1";
		when(chatRoomRepository.findById(roomId)).thenReturn(Optional.empty());

		// 실행
		Optional<ChatRoom> result = chatRoomService.findRoomByIdAndUserId(roomId, userId);

		// 검증
		assertThat(result).isEmpty();
		log.info("User {} 채팅방이 존재하지 않음",  roomId);
	}

}






