package com.foodshare.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	Page<ChatMessage> findByChatRoomId(String chatRoomId, Pageable pageable);

	Page<ChatMessage> findByChatRoomIdOrderByTimestampDesc(String chatRoomId, Pageable pageable);
	Optional<ChatMessage> findFirstByChatRoomIdOrderByTimestampDesc(String chatRoomId);
}


