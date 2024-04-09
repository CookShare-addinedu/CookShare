package com.example.foodshare.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.foodshare.domain.ChatRoom;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatRoom, String> {
	List<ChatRoom> findChatRoomsByFirstUserOrSecondUser(String firstUser, String secondUser);
}