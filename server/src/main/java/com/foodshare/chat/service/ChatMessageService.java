package com.foodshare.chat.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.domain.ChatMessage;


@Service
public class ChatMessageService {
	@Autowired
	ChatMessageRepository chatMessageRepository;

	public void addMessageToChatRoom(String chatRoomId, String sender, String messageContent) {
		Objects.requireNonNull(chatRoomId, "chatRoomId 는 비어있으면 안됨");
		Objects.requireNonNull(sender, "Sender 비어있으면 안됨 ");
		Objects.requireNonNull(messageContent, "Message content 비어있으면 안됨 ");

		ChatMessage newMessage = ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.sender(sender)
			.content(messageContent)
			.timestamp(new Date())
			.build();

		chatMessageRepository.save(newMessage);

	}
}