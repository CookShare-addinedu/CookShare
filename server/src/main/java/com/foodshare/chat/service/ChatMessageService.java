package com.foodshare.chat.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodshare.chat.dto.ChatMessageDto;
import com.foodshare.chat.exception.DatabaseServiceUnavailableException;
import com.foodshare.chat.exception.DbErrorHandlingExecutor;
import com.foodshare.chat.mapper.ChatDataMapper;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.UserRoomVisibilityRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.UserRoomVisibility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatMessageService {
	@Autowired
	ChatMessageRepository chatMessageRepository;

	@Autowired
	ChatDataMapper chatDataMapper;

	@Autowired
	UserRoomVisibilityRepository userRoomVisibilityRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	public void addMessageToChatRoom(String chatRoomId, String senderId, String messageContent) {
		try {
			String receiverId = identifyReceiver(chatRoomId, senderId);
			log.info("처리 중인 채팅 메시지: 발신자 ID={}, 수신자 ID={}", senderId, receiverId);

			performDatabaseOperations(chatRoomId, senderId, receiverId, messageContent);
		} catch (DatabaseServiceUnavailableException e) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "데이터베이스 용할 수 없습니다. 나중에 다시 시도해주세요.",
				e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.", e);
		}
	}

	private String identifyReceiver(String chatRoomId, String senderId) {
		String[] userIds = chatRoomId.split("_");
		if (userIds[0].equals(senderId)) {
			return userIds[1];
		} else {
			return userIds[0];
		}
	}

	private void performDatabaseOperations(String chatRoomId, String senderId, String receiverId,
		String messageContent) {

		saveNewMessage(chatRoomId, senderId, messageContent);
		unHideChatRoomIfNeeded(receiverId, chatRoomId);
	}

	private void saveNewMessage(String chatRoomId, String sender, String messageContent) {
		log.info("메시지 저장 중");

		DbErrorHandlingExecutor.executeDatabaseOperation(() ->
			createAndSaveMessage(chatRoomId, sender, messageContent), "새 메시지 저장 실패");
	}

	private void createAndSaveMessage(String chatRoomId, String sender, String messageContent) {
		ChatMessage newMessage = ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.sender(sender)
			.content(messageContent)
			.timestamp(new Date())
			.build();
		chatMessageRepository.save(newMessage);
	}

	private void unHideChatRoomIfNeeded(String userId, String chatRoomId) {
		log.info("채팅방 안숨숨김 업데이트 중");

		DbErrorHandlingExecutor.executeDatabaseOperation(() ->
			updateChatRoomVisibility(userId, chatRoomId), "채팅방 숨김 해제 실패");
	}

	private void updateChatRoomVisibility(String userId, String chatRoomId) {
		Query query = new Query(Criteria.where("userId").is(userId).and("chatRoomId").is(chatRoomId));
		Update update = new Update().set("isHidden", false);

		mongoTemplate.updateFirst(query, update, UserRoomVisibility.class);
	}

	public Slice<ChatMessageDto> listMessagesInChatRoom(String chatRoomId, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

		Slice<ChatMessage> messages = getMessages(chatRoomId, pageable);

		return toChatMessageDtoSlice(messages);
	}

	private Slice<ChatMessage> getMessages(String chatRoomId, PageRequest pageable) {
		try {
			return chatMessageRepository.findByChatRoomIdOrderByTimestampDesc(chatRoomId, pageable);
		} catch (DataAccessException ex) {
			log.error("데이터베이스에서 메시지를 조회하는 도중 오류가 발생했습니다. 채팅방 ID: {}", chatRoomId, ex);
			throw new DatabaseServiceUnavailableException("메시지 조회 서비스를 사용할 수 없습니다. 나중에 다시 시도해주세요.", ex);
		}
	}

	private Slice<ChatMessageDto> toChatMessageDtoSlice(Slice<ChatMessage> messages) {
		return messages.map(chatDataMapper::toChatMessageDto);
	}
}





