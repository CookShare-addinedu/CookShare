package com.example.foodshare.chat.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.foodshare.chat.repository.ChatMessageRepository;
import com.example.foodshare.domain.ChatRoom;
import com.example.foodshare.utils.ValidationUtils;

@Service
public class ChatMessageService {
	@Autowired
	ChatMessageRepository chatMessageRepository;

	public void addMessageToChatRoom(String chatRoomId, String sender, String messageContent) {

		Objects.requireNonNull(chatRoomId, "chatRoomId 는 비어있으면 안됨");
		Objects.requireNonNull(sender, "Sender 비어있으면 안됨 ");
		Objects.requireNonNull(messageContent, "Message content 비어있으면 안됨 ");

		ChatRoom chatRoom = chatMessageRepository.findById(chatRoomId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다: " + chatRoomId));


		ChatRoom.ChatMessages newMessage = new ChatRoom.ChatMessages(sender, messageContent, new Date());
		chatRoom.addMessage(newMessage);
		chatMessageRepository.save(chatRoom);
	}
}