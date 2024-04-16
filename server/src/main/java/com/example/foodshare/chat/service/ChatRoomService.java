package com.example.foodshare.chat.service;

import java.util.Date;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.foodshare.chat.dto.ChatRoomDto;
import com.example.foodshare.chat.repository.ChatRoomRepository;
import com.example.foodshare.domain.ChatRoom;
import com.example.foodshare.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatRoomService {
	@Autowired
	ChatRoomRepository chatRoomRepository;

	public ChatRoom createChatRoom(String firstUserId, String secondUserId) {
		firstUserId = ValidationUtils.validateNotEmpty(firstUserId, "firstUserId");
		secondUserId = ValidationUtils.validateNotEmpty(secondUserId, "secondUserId");

		ChatRoom chatRoom = ChatRoom.builder()
			.firstUser(firstUserId)
			.secondUser(secondUserId)
			.createdAt(new Date())
			.build();
		return chatRoomRepository.save(chatRoom);
	}

	// public List<ChatRoom> findRoomsByUserId(String userId) {
	// 	ValidationUtils.validateNotEmpty(userId, "UserId");
	// 	return chatRoomRepository.findByIdContaining(userId);
	// 	//return chatRoomRepository.findByFirstUserOrSecondUser(userId, userId);
	// }

	public List<ChatRoomDto> findRoomsByUserId(String userId) {
		ValidationUtils.validateNotEmpty(userId, "UserId");
		List<ChatRoom> rooms = chatRoomRepository.findByIdContaining(userId);

		return rooms.stream().map(room -> {
			ChatRoom.ChatMessages lastMessage = room.getContent().isEmpty() ?
				new ChatRoom.ChatMessages("", "", new Date()) : // 빈 메시지와 현재 시간을 반환
				room.getContent().get(room.getContent().size() - 1);

			return ChatRoomDto.builder()
				.chatRoomId(room.getId())
				.lastMessage(lastMessage.getContent())
				.lastMessageTimestamp(lastMessage.getTimestamp())
				.build();
		}).collect(Collectors.toList());
	}

	public Optional<ChatRoom> findRoomByIdAndUserId(String roomId, String userId) {
		ValidationUtils.validateNotEmpty(roomId, "RoomId");
		ValidationUtils.validateNotEmpty(userId, "UserId");
		log.info("채팅 상세보기");

		return chatRoomRepository.findById(roomId);

		//return chatRoomRepository.findById(roomId)
		//.filter(room -> userId.equals(room.getFirstUser()) || userId.equals(room.getSecondUser()));
	}

	public Page<ChatRoom.ChatMessages> findMessagesByRoomId(String roomId, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return chatRoomRepository.findMessagesByRoomId(roomId, pageRequest);
	}

}