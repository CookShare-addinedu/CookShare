package com.foodshare.chat.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import com.foodshare.domain.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

	Page<ChatMessage> findByChatRoomIdOrderByTimestampDesc(String chatRoomId, Pageable pageable);

	Optional<ChatMessage> findFirstByChatRoomIdOrderByTimestampDesc(String chatRoomId);

	Slice<ChatMessage> findByChatRoomIdAndTimestampGreaterThan(String chatRoomId, Date startTimestamp,
		PageRequest pageable);

}


