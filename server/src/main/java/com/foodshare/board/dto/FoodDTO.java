package com.foodshare.board.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
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
	private String status;
	private String title;
	private String description;
}