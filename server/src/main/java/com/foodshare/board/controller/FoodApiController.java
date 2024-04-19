package com.foodshare.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.foodshare.board.exception.FileStorageException;
import com.foodshare.board.service.FileStorageService;
import com.foodshare.board.service.FoodService;
import com.foodshare.domain.Food;
import com.foodshare.board.dto.FoodDTO;

@RequestMapping("/api/foods")
@RestController
public class FoodApiController {
	@Autowired
	private FoodService foodService;
	@Autowired
	private FileStorageService fileStorageService;
	@GetMapping("/{id}")
	public ResponseEntity<FoodDTO> read(@PathVariable("id") Long id) {
		System.out.println("상세보기 요청이 들어왔습니다" + id);
		FoodDTO foodDTO = foodService.read(id);
		return ResponseEntity.ok(foodDTO);
	}

	@GetMapping("")
	public ResponseEntity<Page<FoodDTO>> getAllFoods(@PageableDefault(size = 10) Pageable pageable) {
		Page<FoodDTO> foodDTOs = foodService.getAllFoods(pageable);
		return ResponseEntity.ok(foodDTOs);
	}

	@PostMapping("")
	public ResponseEntity<Food> create(@ModelAttribute FoodDTO foodDTO) {
		List<String> fileDownloadUrls = new ArrayList<>();
		try {
			if (foodDTO.getImages() != null && !foodDTO.getImages().isEmpty()) {
				for (MultipartFile image : foodDTO.getImages()) {
					String fileName = fileStorageService.storeFile(image);
					String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/download/")
						.path(fileName)
						.toUriString();
					fileDownloadUrls.add(fileDownloadUrl); // 이미지 URL을 리스트에 추가
				}
				foodDTO.setImageUrls(fileDownloadUrls); // foodDTO에 이미지 URL 리스트 설정
			}
		} catch (FileStorageException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		Food createdFood = foodService.create(foodDTO); // 수정된 foodDTO 전달
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFood);
	}
}