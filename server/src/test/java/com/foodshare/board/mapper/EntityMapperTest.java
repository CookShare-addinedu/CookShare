package com.foodshare.board.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.foodshare.domain.Category;
import com.foodshare.domain.Food;
import com.foodshare.domain.FoodImage;
import com.foodshare.board.dto.FoodDTO;

class EntityMapperTest {
	private EntityMapper entityMapper;
	private FoodDTO foodDTO;
	@BeforeEach
	void setUp() {
		entityMapper = Mappers.getMapper(EntityMapper.class);
		foodDTO = new FoodDTO();
		foodDTO.setCategory("Fruits");
		foodDTO.setTitle("Apple");
		foodDTO.setDescription("Fresh apples");
		foodDTO.setMakeByDate(LocalDate.now());
		foodDTO.setEatByDate(LocalDate.now().plusDays(7));
		foodDTO.setImageUri("image/path/to/apple.jpg");
	}

	@Test
	void convertToFood() {
		Food food = entityMapper.convertToFood(foodDTO);
		assertNotNull(food);
		assertEquals(foodDTO.getTitle(), food.getTitle());
		assertEquals(foodDTO.getDescription(), food.getDescription());
		assertEquals(entityMapper.localDateToTimestamp(foodDTO.getEatByDate()), food.getEatByDate());
		assertEquals(entityMapper.localDateToTimestamp(foodDTO.getMakeByDate()), food.getMakeByDate());
		assertEquals(foodDTO.getStatus(), food.getStatus());
	}

	@Test
	void localDateToTimestampTest() {
		LocalDate localDate = LocalDate.of(2024, 4, 9);
		Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay());
		LocalDate resultDate = timestamp.toLocalDateTime().toLocalDate();
		assertEquals(localDate, resultDate);
	}

	@Test
	void convertToFoodImageTest() {
		Food food = entityMapper.convertToFood(foodDTO);
		FoodImage foodImage = entityMapper.convertToFoodImage(foodDTO, food);
		assertNotNull(foodImage);
		assertEquals(foodDTO.getImageUri(), foodImage.getImagePath());
	}

	@Test
	void convertToCategoryTest() {
		Category category = entityMapper.convertToCategory(foodDTO.getCategory());
		assertNotNull(category);
		assertEquals(foodDTO.getCategory(), category.getName());
	}

}