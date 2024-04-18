
package com.foodshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.foodshare.chat.dto.ChatMessageDto;
import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.chat.mapper.ChatDataMapper;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.chat.repository.UserRoomVisibilityRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;
import com.foodshare.domain.UserRoomVisibility;
import com.foodshare.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatRoomService {
	@Autowired
	ChatRoomRepository chatRoomRepository;

	@Autowired
	ChatMessageRepository chatMessageRepository;

	@Autowired
	private UserRoomVisibilityRepository userRoomVisibilityRepository;

	@Autowired
	ChatDataMapper chatDataMapper;

	//채팅방 목록 불러오기
	public List<ChatRoomDto> listChatRoomsForUser(String userId) {
		Set<String> hiddenRoomIds = getHiddenRoomIds(userId);
		return listAvailableChatRooms(userId, hiddenRoomIds);
	}

	private Set<String> getHiddenRoomIds(String userId) {
		return userRoomVisibilityRepository.findByUserId(userId).stream()
			.filter(UserRoomVisibility::isHidden)
			.map(UserRoomVisibility::getChatRoomId)
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
		ChatMessage lastMessage = lastMessageOpt.orElse(new ChatMessage("", room.getId(), "", "", new Date()));
		return chatDataMapper.toChatRoomDto(room, lastMessage);
	}

	//채팅방 숨기기
	public void setRoomHidden(String userId, String chatRoomId) {
		UserRoomVisibility visibility = findOrCreateVisibility(userId, chatRoomId);
		updateVisibilityToHidden(visibility);
	}

	private UserRoomVisibility findOrCreateVisibility(String userId, String chatRoomId) {
		return userRoomVisibilityRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
			.orElse(UserRoomVisibility.builder()
				.userId(userId)
				.chatRoomId(chatRoomId)
				.isHidden(false)
				.build());
	}

	private void updateVisibilityToHidden(UserRoomVisibility visibility) {
		visibility.setHidden(true);
		userRoomVisibilityRepository.save(visibility);
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










