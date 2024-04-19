	package com.foodshare.board.service;

	import java.util.List;

	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import com.foodshare.board.exception.NotFoundException;
	import com.foodshare.domain.Category;
	import com.foodshare.domain.Food;
	import com.foodshare.domain.FoodImage;
	import com.foodshare.board.dto.FoodDTO;
	import com.foodshare.board.mapper.EntityMapper;
	import com.foodshare.board.repository.CategoryRepository;
	import com.foodshare.board.repository.FoodImageRepository;
	import com.foodshare.board.repository.FoodRepository;

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
		public Page<FoodDTO> getAllFoods(Pageable pageable) {
			Page<Food> pageFoods = foodRepository.findAll(pageable);
			return pageFoods.map(food -> {
				List<FoodImage> foodImages = foodImageRepository.findByFoodFoodId(food.getFoodId());
				Category category = categoryRepository.findById(food.getCategory().getCategoryId()).orElse(null);
				// System.out.println(entityMapper.convertToFoodDTO(food, foodImages, category).getFoodId());
				return entityMapper.convertToFoodDTO(food, foodImages, category);
			});
		}

		public FoodDTO read(Long id) {
			Food food = foodRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Food not found with id: " + id));
			List<FoodImage> foodImages = foodImageRepository.findByFoodFoodId(food.getFoodId());
			Category category = categoryRepository.findById(food.getCategory().getCategoryId())
				.orElseThrow(() -> new NotFoundException("Category not found"));
			return entityMapper.convertToFoodDTO(food, foodImages, category);
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