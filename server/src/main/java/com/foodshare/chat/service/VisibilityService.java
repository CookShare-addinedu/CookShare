package com.foodshare.chat.service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.foodshare.chat.annotation.LogExecutionTime;
import com.foodshare.chat.exception.DbErrorHandlingExecutor;
import com.foodshare.chat.repository.UserChatRoomVisibilityRepository;
import com.foodshare.domain.UserChatRoomVisibility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisibilityService {
	private final UserChatRoomVisibilityRepository userChatRoomVisibilityRepository;
	private final MongoTemplate mongoTemplate;
	private final MongoQueryBuilder mongoQueryBuilder;
	@LogExecutionTime
	public void setRoomHidden(String userId, String chatRoomId) {
		log.info("setRoomHidden: userId={}, chatRoomId ID={}", userId, chatRoomId);

		UserChatRoomVisibility visibility = findOrCreateVisibility(userId, chatRoomId);
		updateVisibilityToHidden(visibility);
		mongoQueryBuilder.updateMessagesAsRead(chatRoomId, userId);
	}

	public UserChatRoomVisibility findOrCreateVisibility(String userId, String chatRoomId) {
		log.info("findOrCreateVisibility: userId={}, chatRoomId ID={}", userId, chatRoomId);
		return userChatRoomVisibilityRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
			.orElse(UserChatRoomVisibility.builder()
				.userId(userId)
				.chatRoomId(chatRoomId)
				.isHidden(false)
				.lastHiddenTimestamp(new Date())
				.build());
	}

	public void updateVisibilityToHidden(UserChatRoomVisibility visibility) {
		log.info("updateVisibilityToHidde: visibility={}", visibility);
		visibility.setHidden(true);
		visibility.setLastHiddenTimestamp(new Date());
		userChatRoomVisibilityRepository.save(visibility);
	}

	public void unHideChatRoomIfNeeded(String userId, String chatRoomId) {
		log.info("unHideChatRoomIfNeeded:  userId={}, chatRoomId ID={}", userId, chatRoomId);

		DbErrorHandlingExecutor.executeDatabaseOperation(() ->
			mongoQueryBuilder.updateChatRoomVisibility(userId, chatRoomId), "채팅방 숨김 해제 실패");
	}

	public Set<String> getHiddenRoomIds(String userId) {
		log.info("getHiddenRoomId:  userId={}", userId);

		return userChatRoomVisibilityRepository.findByUserId(userId).stream()
			.filter(UserChatRoomVisibility::isHidden)
			.map(UserChatRoomVisibility::getChatRoomId)
			.collect(Collectors.toSet());
	}

	public Optional<UserChatRoomVisibility> getUserChatRoomVisibility(String userId, String chatRoomId) {
		log.info("getUserChatRoomVisibility:  userId={}, chatRoomId ID={}", userId, chatRoomId);

		return userChatRoomVisibilityRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
	}

}
