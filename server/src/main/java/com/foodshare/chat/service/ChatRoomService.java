package com.foodshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.foodshare.chat.dto.ChatMessageDTO;
import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;
import com.foodshare.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatRoomService {
	@Autowired
	ChatRoomRepository chatRoomRepository;

	@Autowired
	ChatMessageRepository chatMessageRepository;

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

	//채팅 리스트
	public List<ChatRoomDto> findRoomsByUserId(String userId) {
		ValidationUtils.validateNotEmpty(userId, "UserId");
		List<ChatRoom> rooms = chatRoomRepository.findByIdContaining(userId);

		return rooms.stream().map(room -> {
			Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findFirstByChatRoomIdOrderByTimestampDesc(
				room.getId());
			ChatMessage lastMessage = lastMessageOpt.orElse(
				new ChatMessage("", room.getId(), "", "", new Date()));

			return ChatRoomDto.builder()
				.chatRoomId(room.getId())
				.lastMessage(lastMessage.getContent())
				.lastMessageTimestamp(lastMessage.getTimestamp())
				.build();
		}).collect(Collectors.toList());
	}

	// 채팅 상세
	public Slice<ChatMessageDTO> getMessagesByChatRoomId(String chatRoomId, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
		Slice<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByTimestampDesc(chatRoomId, pageable);
		return messages.map(this::convertToDTO);
	}

	private ChatMessageDTO convertToDTO(ChatMessage message) {
		return ChatMessageDTO.builder()
			.chatRoomId(message.getChatRoomId())
			.sender(message.getSender())
			.content(message.getContent())
			.timestamp(message.getTimestamp())
			.build();
	}

	//목록 사용하지 않음
	public Optional<ChatRoom> findRoomByIdAndUserId(String roomId, String userId) {
		ValidationUtils.validateNotEmpty(roomId, "RoomId");
		ValidationUtils.validateNotEmpty(userId, "UserId");
		log.info("채팅 상세보기");

		return chatRoomRepository.findById(roomId);

		//return chatRoomRepository.findById(roomId)
		//.filter(room -> userId.equals(room.getFirstUser()) || userId.equals(room.getSecondUser()));
	}

}

