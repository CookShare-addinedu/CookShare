package com.foodshare.notification.sse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.foodshare.domain.Notification;
import com.foodshare.notification.service.NotificationService;
import com.foodshare.notification.sse.component.SseEmitters;
import com.foodshare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseEmitterService {
	private final SseEmitters sseEmitters;

	public void sendNotification(String userId, Object data) {
		SseEmitter emitter = sseEmitters.getEmitter(userId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("notification").data(data));
			} catch (Exception e) {
				sseEmitters.remove(userId, emitter);
			}
		}
	}
}

