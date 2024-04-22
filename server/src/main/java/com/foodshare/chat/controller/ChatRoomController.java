package com.foodshare.chat.controller;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodshare.chat.dto.ChatMessageDto;
import com.foodshare.chat.dto.ChatRoomCreationDto;
import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.chat.service.ChatMessageService;
import com.foodshare.chat.service.ChatRoomService;
import com.foodshare.domain.ChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j

@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;

	@GetMapping("/detailRoom/{chatRoomId}/messages")
	public ResponseEntity<Slice<ChatMessageDto>> listChatRoomMessages(
		@PathVariable String chatRoomId,
		@RequestParam String userId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		log.info("페이징 메시지 상세 조회 시작 ");

		Slice<ChatMessageDto> messages = chatMessageService.listMessagesInChatRoom(chatRoomId, userId, page, size);
		log.info("메시지 내용: {}", messages.getContent());

		if (messages.isEmpty()) {
			return ResponseEntity.noContent().build(); // 나중에 예외처리로 ?
		}

		return ResponseEntity.ok(messages);
	}

	@GetMapping("/ListRooms")
	public ResponseEntity<List<ChatRoomDto>> listChatRooms(@RequestParam String userId) {
		log.info("채팅 목록 조회 시작 ");

		List<ChatRoomDto> chatRoomDto = chatRoomService.listChatRoomsForUser(userId);
		return ResponseEntity.ok(chatRoomDto);
	}

	// 채팅방 생성
	@PostMapping("/createRoom")
	public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomCreationDto chatRoomCreationDto) {
		ChatRoom newRoom = chatRoomService.createChatRoom(chatRoomCreationDto.getFirstUserMobileNumber(),
			chatRoomCreationDto.getSecondUserMobileNumber());

		return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
	}

	@PutMapping("/hideRoom/{chatRoomId}")
	public void hideChatRoom(@PathVariable String chatRoomId, @RequestParam String userId) {
		chatRoomService.setRoomHidden(userId, chatRoomId);
	}
}



