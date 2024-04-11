	package com.foodshare.service;

	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import com.foodshare.domain.Category;
	import com.foodshare.domain.Food;
	import com.foodshare.domain.FoodImage;
	import com.foodshare.dto.FoodDTO;
	import com.foodshare.mapper.EntityMapper;
	import com.foodshare.repository.CategoryRepository;
	import com.foodshare.repository.FoodImageRepository;
	import com.foodshare.repository.FoodRepository;

	@Service
	@Transactional
	public class FoodService {
		private final FoodRepository foodRepository;
		private final FoodImageRepository foodImageRepository;
		private final CategoryRepository categoryRepository;
		private final EntityMapper entityMapper;

		public FoodService(FoodRepository foodRepository, FoodImageRepository foodImageRepository, CategoryRepository categoryRepository, EntityMapper entityMapper) {
			this.foodRepository = foodRepository;
			this.foodImageRepository = foodImageRepository;
			this.categoryRepository = categoryRepository;
			this.entityMapper = entityMapper;
		}

		public Food create(FoodDTO foodDTO) {
			Category category = entityMapper.convertToCategory(foodDTO.getCategory());
			category = categoryRepository.save(category);

			Food food = entityMapper.convertToFood(foodDTO);
			food.setCategory(category);
			food = foodRepository.save(food);

			FoodImage foodImage = entityMapper.convertToFoodImage(foodDTO, food);
			if (foodImage != null) {
				foodImageRepository.save(foodImage);
			}
			return food;
		}
	}