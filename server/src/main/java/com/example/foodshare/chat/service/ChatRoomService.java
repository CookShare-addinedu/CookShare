package com.example.foodshare.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(readOnly = true)
	public List<ChatRoom> findRoomsByUserId(String userId) {
		ValidationUtils.validateNotEmpty(userId, "UserId");

		return chatRoomRepository.findByFirstUserOrSecondUser(userId, userId);
	}

	public Optional<ChatRoom> findRoomByIdAndUserId(String roomId, String userId) {
		ValidationUtils.validateNotEmpty(roomId, "RoomId");
		ValidationUtils.validateNotEmpty(userId, "UserId");
		log.info("채팅 상세보기");

		return chatRoomRepository.findById(roomId);

		//return chatRoomRepository.findById(roomId)
		//.filter(room -> userId.equals(room.getFirstUser()) || userId.equals(room.getSecondUser()));
	}

}