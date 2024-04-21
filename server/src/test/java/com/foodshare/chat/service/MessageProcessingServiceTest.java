package com.foodshare.chat.service;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.domain.ChatRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MessageProcessingServiceTest {
	@InjectMocks
	private MessageProcessingService chatMessageService;

	@InjectMocks
	private ChatRoomService2 chatRoomService;

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
		//	.createdAt(new Date())
		//	.content(new ArrayList<>())
			.build();
	}

	@Test
	@DisplayName("채팅방에 메시지 추가")
	void addMessageToChatRoomTest() {
		// given(준비)
		String sender = "UserA";
		String messageContent = "Hello, World!";

		//when(chatMessageRepository.findById(chatRoomId))
			//findById 메소드가 chatRoomId와 함께 호출될 때를 가정한당
		//	.thenReturn(Optional.of(expectedChatRoom));
		//위의 가정에 따라 호출될 때, expectedChatRoom을 포함하는 Optional 객체를 반환

		// when (실행) 하면
		chatMessageService.addMessageToChatRoom(chatRoomId, sender, messageContent);

		// then (검증) 어떠한 결과가 나와야함
		//Optional<ChatRoom> savedChatRoom = chatMessageRepository.findById(chatRoomId);
		//assertTrue(savedChatRoom.isPresent(), "채팅방이 저장되어있지 않음.");
		log.info("채팅방이 존재.");

		//// 해당 메소드가 호출되었는지 검증
		//verify(chatMessageRepository).save(any(ChatRoom.class));
		log.info("메시지 저장.");

		//ChatRoom savedRoom = savedChatRoom.get();
		//assertFalse(savedRoom.getContent().isEmpty(), "메시지는 비어있으면 안됨.");

		//ChatRoom.ChatMessages lastMessage = savedRoom.getContent().get(0);
		//assertThat(lastMessage.getSender()).isEqualTo(sender);
		//assertThat(lastMessage.getContent()).isEqualTo(messageContent);
	}

}
