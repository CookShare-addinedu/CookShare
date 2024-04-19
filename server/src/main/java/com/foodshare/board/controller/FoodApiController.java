package com.foodshare.board.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.foodshare.board.dto.FoodDTO;
import com.foodshare.board.exception.FileStorageException;
import com.foodshare.board.service.FileStorageService;
import com.foodshare.board.service.FoodService;
import com.foodshare.domain.Food;


@RequestMapping("/api/foods")
@RestController
public class FoodApiController {
	@Autowired
	private FoodService foodService;
	@Autowired
	private FileStorageService fileStorageService;
	private static final Logger log = LoggerFactory.getLogger(FoodApiController.class);


	@GetMapping("/{id}")
	public ResponseEntity<FoodDTO> read(@PathVariable("id") Long id) {
		FoodDTO foodDTO = foodService.read(id);
		return ResponseEntity.ok(foodDTO);
	}

	@GetMapping
	public ResponseEntity<Page<FoodDTO>> getAllFoods(@PageableDefault(size = 10) Pageable pageable) {
		Page<FoodDTO> foodDTOs = foodService.getAllFoods(pageable);
		return ResponseEntity.ok(foodDTOs);
	}

	@PostMapping
	public ResponseEntity<?> create(@ModelAttribute FoodDTO foodDTO) {
		foodDTO.setImageUrls(processUploadedFiles(foodDTO.getImages()));
		Food createdFood = foodService.create(foodDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFood);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @ModelAttribute FoodDTO foodDTO) {
		log.info("Updating food with id: {}", id);
		FoodDTO existingFoodDTO = foodService.read(id);
		if (foodDTO.getImages() != null && !foodDTO.getImages().isEmpty()) {
			foodDTO.setImageUrls(processUploadedFiles(foodDTO.getImages()));
		} else {
			foodDTO.setImageUrls(existingFoodDTO.getImageUrls());
		}
		Food updatedFood = foodService.update(id, foodDTO);
		return ResponseEntity.ok(updatedFood);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		foodService.delete(id);
		return ResponseEntity.ok().build();
	}

	private List<String> processUploadedFiles(List<MultipartFile> images) {
		List<String> fileDownloadUrls = null;
		try {
			fileDownloadUrls = fileStorageService.storeFiles(images);
		} catch (FileStorageException e) {
			throw new RuntimeException(e);
		}
		return fileDownloadUrls;
	}
}
