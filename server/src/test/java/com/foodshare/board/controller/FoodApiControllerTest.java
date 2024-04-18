package com.foodshare.board.controller;

import com.foodshare.domain.Food;
import com.foodshare.board.dto.FoodDTO;
import com.foodshare.board.service.FileStorageService;
import com.foodshare.board.service.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodApiController.class)
public class FoodApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FoodService foodService;

	@MockBean
	private FileStorageService fileStorageService;

	@Test
	public void createFood_Success() throws Exception {
		// Given
		FoodDTO foodDTO = new FoodDTO(); // 필요한 속성 설정
		Food createdFood = new Food(); // 생성된 Food 객체에 대한 가정 설정
		MockMultipartFile image = new MockMultipartFile("image", "filename.txt", "text/plain", "some xml".getBytes());

		given(fileStorageService.storeFile(image)).willReturn("filename.txt");
		given(foodService.create(foodDTO)).willReturn(createdFood);

		// When & Then
		mockMvc.perform(multipart("/api/foods")
				.file(image)
				.param("title", "Apple") // FoodDTO의 다른 필드들에 대한 param 설정
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isCreated());
	}
}