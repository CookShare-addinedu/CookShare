package com.foodshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodshare.chat.annotation.LogExecutionTime;
import com.foodshare.chat.dto.ChatRoomCreationDto;
import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.chat.mapper.ChatDataMapper;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;
import com.foodshare.chat.utils.ValidationUtils;
import com.foodshare.domain.User;
import com.foodshare.security.repository.UserRepository;
import com.foodshare.security.service.UserServiceImpl;

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
	private final UserServiceImpl userService;
	private final MongoQueryBuilder mongoQueryBuilder;

	@LogExecutionTime
	public List<ChatRoomDto> listChatRoomsForUser(String userId) {
		log.debug("listChatRoomsForUser: userId={}", userId);

		Set<String> hiddenRoomIds = visibilityService.getHiddenRoomIds(userId);

		List<ChatRoomDto> chatRooms = listAvailableChatRooms(userId, hiddenRoomIds);

		updateUnreadCounts(chatRooms, userId);

		return chatRooms;
	}

	private ChatRoomDto convertRoomToDto(ChatRoom room) {
		log.debug("convertRoomToDto: roomId={}", room.getUrlIdentifier());

		ChatMessage lastMessage = chatMessageRepository.findFirstByChatRoomIdOrderByTimestampDesc(
				room.getUrlIdentifier())
			.orElse(ChatMessage.builder()
				.chatRoomId(room.getUrlIdentifier())
				.sender("")
				.content("빈 메시지")
				.timestamp(new Date())
				.isRead(false)
				.build());
		log.debug("마지막 메시지: {}", lastMessage);

		return chatDataMapper.toChatRoomDto(room, lastMessage);
	}

	private List<ChatRoomDto> listAvailableChatRooms(String userId, Set<String> hiddenRoomIds) {
		log.debug(" listAvailableChatRooms");
		return chatRoomRepository.findByIdContaining(userId).stream()
			.filter(room -> !hiddenRoomIds.contains(room.getUrlIdentifier()))
			.map(this::convertRoomToDto)
			.collect(Collectors.toList());
	}

	private void updateUnreadCounts(List<ChatRoomDto> chatRooms, String userId) {
		chatRooms.forEach(room -> {
			long unreadCount = mongoQueryBuilder.countUnreadMessages(room.getChatRoomId(), userId);
			room.setUnreadCount(unreadCount);
			log.debug("채팅방 ID={}, 읽지 않은 메시지 수={}", room.getChatRoomId(), unreadCount);
		});
	}


	public ChatRoom createChatRoom(String firstUserId, String secondUserId, Long foodId, String chatRoomId,
		String chaRoomUrlId) {
		log.debug("createChatRoom");
		ValidationUtils.validateNotEmpty(firstUserId, "firstUserId");
		ValidationUtils.validateNotEmpty(secondUserId, "secondUserId");

		ChatRoom chatRoom = ChatRoom.builder()
			.id(chatRoomId)
			.firstUser(firstUserId)
			.secondUser(secondUserId)
			.foodId(foodId)
			.urlIdentifier(chaRoomUrlId)
			.build();
		log.debug("chatRoomId={}", chatRoom);
		return chatRoomRepository.save(chatRoom);
	}

	public ChatRoom findOrCreateChatRoom(String firstUserId, String secondUserId, Long foodId) {
		Optional<User> firstUserNickName = userService.findByMobileNumber(firstUserId);
		Optional<User> secondUserNickName = userService.findByMobileNumber(secondUserId);
		String chatRoomUrlId = firstUserNickName + "_" + secondUserNickName;

		Optional<ChatRoom> existingChatRoom = findByUrlIdentifier(chatRoomUrlId);
		return existingChatRoom.orElseGet(() -> {
			String chatRoomId = firstUserId + "_" + secondUserId;
			return createChatRoom(firstUserId, secondUserId, foodId, chatRoomId, chatRoomUrlId);
		});
	}

	public ChatRoomCreationDto toChatRoomCreationDto(ChatRoom room) {
		return chatDataMapper.toChatRoomCreationDto(room);
	}

	public Optional<ChatRoom> findByUrlIdentifier(String chatRoomUrlId) {
		return chatRoomRepository.findByUrlIdentifier(chatRoomUrlId);
	}

}
