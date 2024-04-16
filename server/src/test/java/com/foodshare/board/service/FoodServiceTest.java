	package com.foodshare.board.service;

	import org.junit.jupiter.api.Test;
	import org.junit.jupiter.api.extension.ExtendWith;
	import org.mockito.InjectMocks;
	import org.mockito.Mock;
	import org.mockito.junit.jupiter.MockitoExtension;

	import static org.junit.jupiter.api.Assertions.*;
	import static org.mockito.ArgumentMatchers.*;
	import static org.mockito.Mockito.verify;
	import static org.mockito.Mockito.when;

	import java.sql.Timestamp;
	import java.time.LocalDate;

	import com.foodshare.domain.Category;
	import com.foodshare.domain.Food;
	import com.foodshare.domain.FoodImage;
	import com.foodshare.board.dto.FoodDTO;
	import com.foodshare.board.mapper.EntityMapper;
	import com.foodshare.board.repository.CategoryRepository;
	import com.foodshare.board.repository.FoodImageRepository;
	import com.foodshare.board.repository.FoodRepository;

	@ExtendWith(MockitoExtension.class)
	public class FoodServiceTest {

		@Mock
		private FoodRepository foodRepository;

		@Mock
		private FoodImageRepository foodImageRepository;

		@Mock
		private CategoryRepository categoryRepository;

		@Mock
		private EntityMapper entityMapper;

		@InjectMocks
		private FoodService foodService;

		@Test
		void whenCreateFood_thenFoodShouldBeCreatedAndSaved() {
			// Given
			FoodDTO foodDTO = new FoodDTO();
			foodDTO.setCategory("Fruits");
			foodDTO.setTitle("Apple");
			foodDTO.setDescription("Fresh apples");
			foodDTO.setMakeByDate(LocalDate.now());
			foodDTO.setEatByDate(LocalDate.now().plusDays(7));
			foodDTO.setImageUri("image/path/to/apple.jpg");

			Category mockCategory = new Category();
			mockCategory.setName(foodDTO.getCategory());

			Food mockFood = new Food();
			mockFood.setTitle(foodDTO.getTitle());
			mockFood.setDescription(foodDTO.getDescription());
			mockFood.setMakeByDate(Timestamp.valueOf(foodDTO.getMakeByDate().atStartOfDay()));
			mockFood.setEatByDate(Timestamp.valueOf(foodDTO.getEatByDate().atStartOfDay()));

			FoodImage mockFoodImage = new FoodImage();
			mockFoodImage.setFood(mockFood);
			mockFoodImage.setImagePath(foodDTO.getImageUri());

			when(entityMapper.convertToCategory(any(String.class))).thenReturn(mockCategory);
			when(entityMapper.convertToFood(any(FoodDTO.class))).thenReturn(mockFood);
			when(entityMapper.convertToFoodImage(any(FoodDTO.class), any(Food.class))).thenReturn(mockFoodImage);
			when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);
			when(foodRepository.save(any(Food.class))).thenReturn(mockFood);
			when(foodImageRepository.save(any(FoodImage.class))).thenReturn(mockFoodImage);

			// When
			Food createdFood = foodService.create(foodDTO);

			// Then
			assertNotNull(createdFood, "The created Food should not be null");
			assertEquals(foodDTO.getTitle(), createdFood.getTitle(), "Title should match");
			assertEquals(foodDTO.getDescription(), createdFood.getDescription(), "Description should match");

			verify(categoryRepository).save(any(Category.class));
			verify(foodRepository).save(any(Food.class));
			verify(foodImageRepository).save(any(FoodImage.class));

		}
	}

