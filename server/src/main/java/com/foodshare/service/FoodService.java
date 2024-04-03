package com.foodshare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodshare.domain.Food;
import com.foodshare.repository.FoodRepository;

@Service
public class FoodService {
	@Autowired
	private FoodRepository foodRepository;

	public List<Food> findAll() {
		return foodRepository.findAll();
	}

	public Optional<Food> read(Long foodId) {
		return foodRepository.findById(foodId);
	}

	public Food create(Food food) {
		return foodRepository.save(food);
	}

	public Food update(Food food) {
		// 여기에는 존재하는 Food를 업데이트하는 로직 추가
		return foodRepository.save(food);
	}

	public void delete(Long foodId) {
		foodRepository.deleteById(foodId);
	}
}

