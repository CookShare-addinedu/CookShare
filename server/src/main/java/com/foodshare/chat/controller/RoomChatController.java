package com.foodshare.chat.controller;import java.time.LocalDateTime;import java.util.List;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.domain.Page;import org.springframework.data.domain.PageRequest;import org.springframework.data.domain.Pageable;import org.springframework.data.domain.Slice;import org.springframework.format.annotation.DateTimeFormat;import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PathVariable;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestBody;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;import org.springframework.web.bind.annotation.RestController;import org.springframework.web.server.ResponseStatusException;import com.foodshare.chat.dto.ChatMessageDTO;import com.foodshare.chat.dto.ChatRoomCreationDto;import com.foodshare.chat.dto.ChatRoomDto;import com.foodshare.chat.service.ChatRoomService;import com.foodshare.domain.ChatMessage;import com.foodshare.domain.ChatRoom;import lombok.extern.slf4j.Slf4j;@RestController@RequestMapping("/api")@Slf4jpublic class RoomChatController {	@Autowired	ChatRoomService chatRoomService;	@GetMapping("/detailRoom/{chatRoomId}/messages")	public ResponseEntity<Slice<ChatMessageDTO>> getMessageList(@PathVariable String chatRoomId,		@RequestParam(defaultValue = "0") int page,		@RequestParam(defaultValue = "10") int size) {		log.info("페이징 메시지 상세 조회 시작 ");		Slice<ChatMessageDTO> messages = chatRoomService.getMessagesByChatRoomId(chatRoomId, page, size);		log.info("메시지 내용: {}", messages.getContent());		if (messages.isEmpty()) {			return ResponseEntity.noContent().build();		}		return ResponseEntity.ok(messages);	}	@GetMapping("/ListRooms")	public ResponseEntity<List<ChatRoomDto>> getUserRooms(@RequestParam String userId) {		List<ChatRoomDto> chatRoomDto = chatRoomService.findRoomsByUserId(userId);		return ResponseEntity.ok(chatRoomDto);	}	// 채팅방 생성	@PostMapping("/createRoom")	public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomCreationDto chatRoomCreationDto) {		ChatRoom newRoom = chatRoomService.createChatRoom(chatRoomCreationDto.getFirstUserMobileNumber(),			chatRoomCreationDto.getSecondUserMobileNumber());		return new ResponseEntity<>(newRoom, HttpStatus.CREATED);	}}