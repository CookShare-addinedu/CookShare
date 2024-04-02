package com.example.foodshare.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.foodshare.domain.ChatRoom;


@Repository
public interface ChatRepository extends MongoRepository<ChatRoom, String> {
}
