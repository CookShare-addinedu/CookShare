package com.foodshare.dto;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
	private MultipartFile image;
	private String imageUri;
	private String category;
	private LocalDate makeByDate;
	private LocalDate eatByDate;
	private String status;
	private String title;
	private String description;
}