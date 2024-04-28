package com.foodshare.chat.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodshare.chat.annotation.LogExecutionTime;
import com.foodshare.chat.exception.DatabaseServiceUnavailableException;
import com.foodshare.chat.exception.DbErrorHandlingExecutor;
import com.foodshare.chat.repository.ChatMessageRepository;
import com.foodshare.chat.repository.ChatRoomRepository;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;
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

	private final SseEmitterService sseEmitterService;
	private final ChatRoomRepository chatRoomRepository;
	private final MongoQueryBuilder mongoQueryBuilder;

	private static final Date EPOCH = new Date(0);

	@LogExecutionTime
	public void addMessageToChatRoom(String chatRoomId, String senderId, String messageContent) {
		try {
			String receiverId = identifyReceiver(chatRoomId, senderId);
			log.info("처리 중인 채팅 메시지: 발신자 ID={}, 수신자 ID={}", senderId, receiverId);

			performDatabaseOperations(chatRoomId, senderId, receiverId, messageContent);
			sseEmitterService.processNotification(receiverId, senderId, messageContent);

			long totalUnreadCount = mongoQueryBuilder.countTotalUnreadMessages(receiverId);
			sseEmitterService.sendUnreadCountUpdate(receiverId, totalUnreadCount);


		} catch (DatabaseServiceUnavailableException e) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "데이터베이스 용할 수 없습니다. 나중에 다시 시도해주세요.",
				e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.", e);
		}
	}

	private String identifyReceiver(String chatRoomUrlId, String senderId) {
		log.info("identifyReceiver: chatRoomUrlId={}, senderId={}", chatRoomUrlId, senderId);

		return chatRoomRepository
			.findByUrlIdentifier(chatRoomUrlId) // Optional<ChatRoom>
			.map(chatRoom -> {
				if (chatRoom.getFirstUser().equals(senderId)) {
					return chatRoom.getSecondUser();
				} else {
					return chatRoom.getFirstUser();
				}
			})
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
	}

	private void performDatabaseOperations(String chatRoomUrlId, String senderId, String receiverId,
		String messageContent) {
		log.info("performDatabaseOperations, chatRoomUrlId={},  senderId={}", chatRoomUrlId, senderId);
		log.info("performDatabaseOperations, receiverIdd={},   messageContent={}", receiverId, messageContent);

		saveNewMessage(chatRoomUrlId, senderId, messageContent);
		visibilityService.unHideChatRoomIfNeeded(receiverId, chatRoomUrlId);
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

// private String identifyReceiver(String chatRoomUrlId, String senderId) {
// 	log.info("identifyReceiver: chatRoomUrlId={},  senderId={}", chatRoomUrlId, senderId);
//
// 	Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByUrlIdentifier(chatRoomUrlId);
//
// 	if (chatRoomOpt.isPresent()) {
// 		ChatRoom chatRoom = chatRoomOpt.get();
//
// 		if (chatRoom.getFirstUser().equals(senderId)) {
// 			return chatRoom.getSecondUser();
// 		} else {
// 			return chatRoom.getFirstUser();
// 		}
// 	} else {
// 		throw new IllegalArgumentException("채팅방을 찾을 수 없습니다.");
// 	}
// }