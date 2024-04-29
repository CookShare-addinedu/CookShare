package com.foodshare.chat.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodshare.chat.annotation.LogExecutionTime;
import com.foodshare.chat.dto.ChatMessageDto;
import com.foodshare.chat.mapper.ChatDataMapper;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.UserChatRoomVisibility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final ChatDataMapper chatDataMapper;
	private final VisibilityService visibilityService;
	private final MongoTemplate mongoTemplate;

	@LogExecutionTime
	public Slice<ChatMessageDto> listMessagesInChatRoom(String chatRoomId, String userId, int page, int size) {
		PageRequest pageable = createPageRequest(page, size);
		Slice<ChatMessageDto> messages = processMessagesBasedOnVisibility(userId, chatRoomId, pageable);

		updateMessagesAsRead(chatRoomId, userId);

		return messages;
	}

	private Slice<ChatMessageDto> processMessagesBasedOnVisibility(String userId, String chatRoomId,
		PageRequest pageable) {
		Optional<UserChatRoomVisibility> visibilityOpt = visibilityService.getUserChatRoomVisibility(userId,
			chatRoomId);
		return visibilityOpt.map(visibility -> filterMessagesByVisibility(visibility, chatRoomId, pageable))
			.orElseGet(() -> getChatRoomMessages(chatRoomId, pageable));
	}

	private Slice<ChatMessageDto> filterMessagesByVisibility(UserChatRoomVisibility visibility, String chatRoomId,
		PageRequest pageable) {
		return Optional.ofNullable(visibility.getLastHiddenTimestamp())
			.map(lastHidden -> getMessagesSince(chatRoomId, lastHidden, pageable))
			.orElseGet(() -> getChatRoomMessages(chatRoomId, pageable));
	}

	public Slice<ChatMessageDto> getChatRoomMessages(String chatRoomId, PageRequest pageable) {
		return chatMessageRepository.findByChatRoomIdOrderByTimestampDesc(chatRoomId, pageable)
			.map(chatDataMapper::toChatMessageDto);
	}

	private Slice<ChatMessageDto> getMessagesSince(String chatRoomId, Date startTimestamp, PageRequest pageable) {
		return chatMessageRepository.findByChatRoomIdAndTimestampGreaterThan(chatRoomId, startTimestamp, pageable)
			.map(chatDataMapper::toChatMessageDto);
	}

	private PageRequest createPageRequest(int page, int size) {
		return PageRequest.of(page, size, Sort.by("timestamp").descending());
	}

	public void updateMessagesAsRead(String chatRoomId, String userId) {
		Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
			.and("sender").ne(userId)
			.and("isRead").is(false));

		Update update = new Update().set("isRead", true);
		log.info("update ={}", update);
		mongoTemplate.updateMulti(query, update, ChatMessage.class);
	}
}