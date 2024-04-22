package com.foodshare.notification.sse.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.foodshare.notification.sse.component.SseEmitters;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SseController {

	private final SseEmitters sseEmitters;

	public SseController(SseEmitters sseEmitters) {
		this.sseEmitters = sseEmitters;
	}

	private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@GetMapping(value = "/sse/connect/{userId}", produces = "text/event-stream")
	public ResponseEntity<SseEmitter> connect(@PathVariable String userId) {
		SseEmitter emitter = new SseEmitter(300_000L); // 5분 연결 유지
		sseEmitters.add(userId, emitter);

		emitter.onCompletion(() -> sseEmitters.remove(userId, emitter));
		emitter.onTimeout(() -> {
			emitter.complete();
			sseEmitters.remove(userId, emitter);
		});

		try {
			emitter.send(SseEmitter.event().name("connect").data("connected"));
		} catch (Exception e) {
			log.error("SSE 연결 오류: {}", e.getMessage());
			emitter.completeWithError(e);
			sseEmitters.remove(userId, emitter);
		}

		// 주기적으로 "heartbeat" 메시지 보내기 (연결 유지를 위해)
		scheduler.scheduleAtFixedRate(() -> {
			try {
				emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}, 0, 60, TimeUnit.SECONDS);

		return ResponseEntity.ok(emitter); // 연결된 SSEEmitter 반환
	}


}