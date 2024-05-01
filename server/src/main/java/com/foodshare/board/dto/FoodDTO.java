package com.foodshare.board.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.multipart.MultipartFile;

import com.foodshare.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
	private Long foodId;
	private List<MultipartFile> images;
	private List<String> imageUrls;
	private String category;
	private LocalDate makeByDate;
	private LocalDate eatByDate;
	private LocalDate createdAt;
	private String status;
	private String title;
	private String description;
	private String location;
	private User giver;
	private User receiver;
}