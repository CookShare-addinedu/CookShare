package com.foodshare.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodshare.domain.UserChatRoomVisibility;

@Repository
public interface UserChatRoomVisibilityRepository extends MongoRepository<UserChatRoomVisibility, String> {
	List<UserChatRoomVisibility> findByUserId(String userId);

	Optional<UserChatRoomVisibility> findByUserIdAndChatRoomId(String userId, String chatRoomId);


}