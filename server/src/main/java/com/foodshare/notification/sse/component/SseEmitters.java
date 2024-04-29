package com.foodshare.notification.sse.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitters {

	private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	public void add(String userId, SseEmitter emitter) {

		emitters.put(userId, emitter);
	}

	public void remove(String userId, SseEmitter emitter) {
		emitters.remove(userId, emitter);
	}

	public SseEmitter getEmitter(String userId) {
		return emitters.get(userId);
	}

	public void sendToAll(String eventName, Object data) {
		List<String> toRemove = new ArrayList<>();
		emitters.forEach((userId, emitter) -> {
			try {
				emitter.send(SseEmitter.event().name(eventName).data(data));
			} catch (Exception e) {
				toRemove.add(userId);
			}
		});
		toRemove.forEach(emitters::remove);
	}
}

