package com.foodshare.board.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodshare.board.exception.NotFoundException;
import com.foodshare.board.repository.FavoriteFoodRepository;
import com.foodshare.board.repository.FoodRepository;
import com.foodshare.domain.FavoriteFood;
import com.foodshare.domain.Food;
import com.foodshare.domain.User;
import com.foodshare.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteFoodService {
	private final FavoriteFoodRepository favoriteFoodRepository;
	private final UserRepository userRepository;
	private final FoodRepository foodRepository;

	public void toggleFavorite(Long foodId, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		Food food = foodRepository.findById(foodId).orElseThrow(() -> new NotFoundException("Food not found"));
		FavoriteFood favoriteFood = favoriteFoodRepository.findByFoodFoodIdAndUserUserId(foodId, userId);

		if (favoriteFood == null) {
			// 찜하기가 없다면 추가
			FavoriteFood newFavorite = new FavoriteFood();
			newFavorite.setUser(user);
			newFavorite.setFood(food);
			newFavorite.setIsFavorite(true);
			favoriteFoodRepository.save(newFavorite);
		} else {
			// 찜하기가 있다면 삭제
			favoriteFoodRepository.delete(favoriteFood);
		}
	}

	public boolean isFavorited(Long foodId, Long userId) {
		FavoriteFood favoriteFood = favoriteFoodRepository.findByFoodFoodIdAndUserUserId(foodId, userId);
		return favoriteFood != null;
	}


	public long countFavorites(Long foodId) {
		return favoriteFoodRepository.countByFoodFoodId(foodId);
	}
}
