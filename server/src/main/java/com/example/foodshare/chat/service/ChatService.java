package com.example.foodshare.chat.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foodshare.chat.repository.ChatRepository;
import com.example.foodshare.domain.ChatRoom;

@Service
public class ChatService {
	@Autowired
	ChatRepository chatRepository;

	public ChatRoom createChatRoom(String firstUserId, String secondUserId) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setFirstUser(firstUserId);
		chatRoom.setSecondUser(secondUserId);
		chatRoom.setCreatedAt(new Date());
		return chatRepository.save(chatRoom);
	}

	public ChatRoom addMessageToChatRoom(String chatRoomId, String sender, String messageContent) {
		ChatRoom chatRoom = chatRepository.findById(chatRoomId).orElseGet(() -> {

			ChatRoom newChatRoom = new ChatRoom();
			newChatRoom.setId(chatRoomId);

			return chatRepository.save(newChatRoom);
		});

		ChatRoom.ChatMessages newMessage = new ChatRoom.ChatMessages(sender, messageContent, new Date());
		chatRoom.addMessage(newMessage);
		return chatRepository.save(chatRoom);
	}


}

