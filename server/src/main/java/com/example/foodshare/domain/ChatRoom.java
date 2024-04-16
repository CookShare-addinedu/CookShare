package com.example.foodshare.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class ChatRoom {
	@Id
	private String id; // MongoDB의 고유 식별자

	private String foodId;
	private String firstUser;
	private String secondUser;
	private String identifier;
	@Builder.Default
	private List<ChatMessages> content = new ArrayList<>(); // 초기화
	private Date createdAt;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ChatMessages {
		private String sender;
		private String content;
		private Date timestamp;
	}

	public void addMessage(ChatMessages message) {

		this.content.add(message);
	}

	public boolean isMember(String userId) {
		return userId.equals(firstUser) || userId.equals(secondUser);
	}

}
