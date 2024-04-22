package com.foodshare.chat.service;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodshare.chat.annotation.LogExecutionTime;
import com.foodshare.chat.exception.DatabaseServiceUnavailableException;
import com.foodshare.chat.exception.DbErrorHandlingExecutor;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.Notification;
import com.foodshare.notification.service.NotificationService;
import com.foodshare.notification.sse.service.SseEmitterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessingService {

	private final ChatMessageRepository chatMessageRepository;
	private final VisibilityService visibilityService;
	private final NotificationService notificationService;
	private final SseEmitterService sseEmitterService;
	private static final Date EPOCH = new Date(0);

	@LogExecutionTime
	public void addMessageToChatRoom(String chatRoomId, String senderId, String messageContent) {
		try {
			String receiverId = identifyReceiver(chatRoomId, senderId);
			log.info("처리 중인 채팅 메시지: 발신자 ID={}, 수신자 ID={}", senderId, receiverId);

			performDatabaseOperations(chatRoomId, senderId, receiverId, messageContent);

			Notification notification = notificationService.createNotificationForMessage(
				receiverId, senderId, messageContent
			);

			sseEmitterService.sendNotification(receiverId, notification);

		} catch (DatabaseServiceUnavailableException e) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "데이터베이스 용할 수 없습니다. 나중에 다시 시도해주세요.",
				e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.", e);
		}
	}

	private String identifyReceiver(String chatRoomId, String senderId) {
		log.info("identifyReceiver: chatRoomI={},  senderId={}", chatRoomId, senderId);

		String[] userIds = chatRoomId.split("_");
		if (userIds[0].equals(senderId)) {
			return userIds[1];
		} else {
			return userIds[0];
		}
	}

	private void performDatabaseOperations(String chatRoomId, String senderId, String receiverId,
		String messageContent) {
		log.info("performDatabaseOperations, chatRoomId={},  senderId={}", chatRoomId, senderId);
		log.info("performDatabaseOperations, receiverIdd={},   messageContent={}", receiverId, messageContent);

		saveNewMessage(chatRoomId, senderId, messageContent);
		visibilityService.unHideChatRoomIfNeeded(receiverId, chatRoomId);
	}

	private void saveNewMessage(String chatRoomId, String sender, String messageContent) {
		log.info("saveNewMessage");

		DbErrorHandlingExecutor.executeDatabaseOperation(() ->
			createAndSaveMessage(chatRoomId, sender, messageContent), "새 메시지 저장 실패");
	}

	private void createAndSaveMessage(String chatRoomId, String sender, String messageContent) {
		log.info("createAndSaveMessage");

		ChatMessage newMessage = ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.sender(sender)
			.content(messageContent)
			.timestamp(new Date())
			.build();
		chatMessageRepository.save(newMessage);
	}

}

