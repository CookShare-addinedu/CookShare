package com.foodshare.notification.sse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.foodshare.domain.Notification;
import com.foodshare.notification.service.NotificationAggregator;
import com.foodshare.notification.service.NotificationService;
import com.foodshare.notification.sse.component.SseEmitters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseEmitterService {
	private final SseEmitters sseEmitters;
	private final NotificationAggregator notificationAggregator;
	private final NotificationService notificationService;

	public void processNotification(String receiverId, String senderId, Object data) {
		if (notificationAggregator.isNotificationAllowed(receiverId)) {
			createAndSendNotification(receiverId, senderId, data.toString());

			notificationAggregator.updateNotificationTime(receiverId);
		} else {
			log.info("알림이 허락되지 않았음");
		}
	}

	private void createAndSendNotification(String receiverId, String senderId, String data) {
		Notification notification = notificationService.createNotificationForMessage(
			receiverId, senderId, data
		);
		sendIndividualNotification(receiverId, notification);
	}

	public void sendIndividualNotification(String receiverId, Object data) {
		SseEmitter emitter = sseEmitters.getEmitter(receiverId);
		log.info("receiverId = {}", receiverId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("notification").data(data));
			} catch (Exception e) {
				sseEmitters.remove(receiverId, emitter);
				log.error("알림보내는거실패  {}", receiverId, e);
			}
		}
	}

}

