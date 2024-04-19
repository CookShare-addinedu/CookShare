
package com.foodshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.chat.mapper.ChatDataMapper;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.chat.repository.UserChatRoomVisibilityRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;
import com.foodshare.domain.UserChatRoomVisibility;
import com.foodshare.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final UserChatRoomVisibilityRepository userChatRoomVisibilityRepository;
	private final ChatDataMapper chatDataMapper;

	//채팅방 목록 불러오기
	public List<ChatRoomDto> listChatRoomsForUser(String userId) {
		Set<String> hiddenRoomIds = getHiddenRoomIds(userId);
		return listAvailableChatRooms(userId, hiddenRoomIds);
	}

	private Set<String> getHiddenRoomIds(String userId) {
		return userChatRoomVisibilityRepository.findByUserId(userId).stream()
			.filter(UserChatRoomVisibility::isHidden)
			.map(UserChatRoomVisibility::getChatRoomId)
			.collect(Collectors.toSet());
	}

	private List<ChatRoomDto> listAvailableChatRooms(String userId, Set<String> hiddenRoomIds) {
		return chatRoomRepository.findByIdContaining(userId).stream()
			.filter(room -> !hiddenRoomIds.contains(room.getId()))
			.map(this::convertRoomToDto)
			.collect(Collectors.toList());
	}

	private ChatRoomDto convertRoomToDto(ChatRoom room) {
		Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findFirstByChatRoomIdOrderByTimestampDesc(
			room.getId());
		ChatMessage lastMessage = lastMessageOpt.orElse(ChatMessage.builder()
			.chatRoomId(room.getId())
			.sender("")
			.content("")
			.timestamp(new Date())
			.isRead(false)
			.build());
		return chatDataMapper.toChatRoomDto(room, lastMessage);
	}

	//채팅방 숨기기
	public void setRoomHidden(String userId, String chatRoomId) {
		UserChatRoomVisibility visibility = findOrCreateVisibility(userId, chatRoomId);
		updateVisibilityToHidden(visibility);
	}

	private UserChatRoomVisibility findOrCreateVisibility(String userId, String chatRoomId) {
		return userChatRoomVisibilityRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
			.orElse(UserChatRoomVisibility.builder()
				.userId(userId)
				.chatRoomId(chatRoomId)
				.isHidden(false)
				.lastHiddenTimestamp(new Date())
				.build());
	}

	private void updateVisibilityToHidden(UserChatRoomVisibility visibility) {
		visibility.setHidden(true);
		visibility.setLastHiddenTimestamp(new Date());
		userChatRoomVisibilityRepository.save(visibility);
	}

	public ChatRoom createChatRoom(String firstUserId, String secondUserId) {
		firstUserId = ValidationUtils.validateNotEmpty(firstUserId, "firstUserId");
		secondUserId = ValidationUtils.validateNotEmpty(secondUserId, "secondUserId");

		ChatRoom chatRoom = ChatRoom.builder()
			.firstUser(firstUserId)
			.secondUser(secondUserId)
			//.createdAt(new Date())
			.build();
		return chatRoomRepository.save(chatRoom);
	}

	// //목록 사용하지 않음
	// public Optional<ChatRoom> findRoomByIdAndUserId(String roomId, String userId) {
	// 	ValidationUtils.validateNotEmpty(roomId, "RoomId");
	// 	ValidationUtils.validateNotEmpty(userId, "UserId");
	// 	log.info("채팅 상세보기");
	//
	// 	return chatRoomRepository.findById(roomId);
	//
	// 	//return chatRoomRepository.findById(roomId)
	// 	//.filter(room -> userId.equals(room.getFirstUser()) || userId.equals(room.getSecondUser()));
	// }

}










