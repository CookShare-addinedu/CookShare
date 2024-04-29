package com.foodshare.notification.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.foodshare.domain.Notification;
import com.foodshare.domain.User;
import com.foodshare.notification.repository.NotificationRepository;
import com.foodshare.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public Notification createNotificationForMessage(String receiverMobileNumber, String senderMobileNumber,
		String messageContent) {

		User receiver = findUserByMobileNumber(receiverMobileNumber);
		User sender = findUserByMobileNumber(senderMobileNumber);

		String individualMessage = createIndividualMessage(sender, messageContent);

		return createNotification(receiver, individualMessage);
	}

	private String createIndividualMessage(User sender, String messageContent) {
		return String.format("%s 사용자가 채팅을 보냈습니다:", sender.getMobileNumber());
	}

	private Notification createNotification(User receiver, String message) {
		Notification notification = Notification.builder()
			.user(receiver)
			.message(message)
			.isRead(false)
			.isSent(true)
			.createdAt(new Timestamp(System.currentTimeMillis()))
			.build();

		return notificationRepository.save(notification);
	}

	private User findUserByMobileNumber(String mobileNumber) {
		return userRepository.findByMobileNumber(mobileNumber).orElseThrow(
			() -> new RuntimeException("사용자를 찾을 수 없습니다.")
		);
	}

}
