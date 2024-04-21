package com.foodshare.chat.mapper;

import com.foodshare.chat.dto.ChatMessageDto;
import com.foodshare.chat.dto.ChatRoomCreationDto;
import com.foodshare.chat.dto.ChatRoomDto;
import com.foodshare.domain.ChatMessage;
import com.foodshare.domain.ChatRoom;

public interface ChatDataMapper {
	ChatMessageDto toChatMessageDto(ChatMessage message);

	ChatRoomDto toChatRoomDto(ChatRoom room, ChatMessage lastMessage);

	ChatRoomCreationDto toChatRoomCreationDto(ChatRoom room);
}
 