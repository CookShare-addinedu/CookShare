package com.example.foodshare.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.foodshare.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
	List<ChatRoom> findByFirstUserOrSecondUser(String firstUserId, String secondUserId);
}
