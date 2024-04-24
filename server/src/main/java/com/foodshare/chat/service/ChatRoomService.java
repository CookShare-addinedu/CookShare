package com.foodshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodshare.chat.annotation.LogExecutionTime;
import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.chat.mapper.ChatDataMapper;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;
import com.foodshare.chat.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final VisibilityService visibilityService;
	private final ChatDataMapper chatDataMapper;
	private final ChatMessageRepository chatMessageRepository;
	private final MongoTemplate mongoTemplate;

	@LogExecutionTime

	public List<ChatRoomDto> listChatRoomsForUser(String userId) {
		log.info("listChatRoomsForUser");

		Set<String> hiddenRoomIds = visibilityService.getHiddenRoomIds(userId);
		List<ChatRoomDto> chatRooms = listAvailableChatRooms(userId, hiddenRoomIds);

		updateUnreadCounts(chatRooms, userId);

		return chatRooms;
	}

	public ChatRoom createChatRoom(String firstUserId, String secondUserId) {
		log.info("createChatRoom");

		ValidationUtils.validateNotEmpty(firstUserId, "firstUserId");
		ValidationUtils.validateNotEmpty(secondUserId, "secondUserId");
		ChatRoom chatRoom = ChatRoom.builder()
			.firstUser(firstUserId)
			.secondUser(secondUserId)
			.build();
		return chatRoomRepository.save(chatRoom);
	}

	public long countUnreadMessages(String chatRoomId, String userId) {
		Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
			.and("sender").ne(userId)
			.and("isRead").is(false));

		return mongoTemplate.count(query, ChatMessage.class);
	}

	private ChatRoomDto convertRoomToDto(ChatRoom room) {
		log.info(" convertRoomToDto");
		ChatMessage lastMessage = chatMessageRepository.findFirstByChatRoomIdOrderByTimestampDesc(room.getId())
			.orElse(ChatMessage.builder()
				.chatRoomId(room.getId())
				.sender("")
				.content("메시지를 입력하세요")
				.timestamp(new Date())
				.isRead(false)
				.build());
		return chatDataMapper.toChatRoomDto(room, lastMessage);
	}

	private List<ChatRoomDto> listAvailableChatRooms(String userId, Set<String> hiddenRoomIds) {
		log.info(" listAvailableChatRooms");
		return chatRoomRepository.findByIdContaining(userId).stream()
			.filter(room -> !hiddenRoomIds.contains(room.getId()))
			.map(this::convertRoomToDto)
			.collect(Collectors.toList());
	}

	private void updateUnreadCounts(List<ChatRoomDto> chatRooms, String userId) {
		chatRooms.forEach(room -> {
			long unreadCount = countUnreadMessages(room.getChatRoomId(), userId);
			room.setUnreadCount(unreadCount);
			log.info("읽지않은 메시지 숫자 = {}", unreadCount);
		});
	}

}
