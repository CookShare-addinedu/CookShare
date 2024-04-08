package com.foodshare.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.foodshare.domain.Food;
import com.foodshare.dto.FoodDTO;
import com.foodshare.exception.FileStorageException;
import com.foodshare.service.FileStorageService;
import com.foodshare.service.FoodService;

@RequestMapping("/api")
@RestController
public class FoodApiController {
	@Autowired
	private FoodService foodService;
	@Autowired
	private FileStorageService fileStorageService;
	@PostMapping("/foods")
	public ResponseEntity<Food> create(@ModelAttribute FoodDTO foodDTO) {
		try {
			if (!foodDTO.getImage().isEmpty()) {
				String fileName = fileStorageService.storeFile(foodDTO.getImage());
				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/download/")
					.path(fileName)
					.toUriString();
				foodDTO.setImageUri(fileDownloadUri); // 이미지 URL 설정
			}
		} catch (FileStorageException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		Food createdFood = foodService.create(foodDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFood);
	}
}