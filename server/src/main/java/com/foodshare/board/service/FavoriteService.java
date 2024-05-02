package com.foodshare.board.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodshare.board.exception.DuplicateFavoriteException;
import com.foodshare.board.exception.NotFoundException;
import com.foodshare.board.repository.FavoriteRepository;
import com.foodshare.board.repository.FoodRepository;
import com.foodshare.domain.FavoriteFood;
import com.foodshare.domain.Food;
import com.foodshare.domain.User;
import com.foodshare.security.dto.CustomUserDetails;
import com.foodshare.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final FoodRepository foodRepository;

	public boolean addFavorite(Long foodId, Long userId) throws DuplicateFavoriteException {
		boolean isExistingFavorite = favoriteRepository.existsByFoodFoodIdAndUserUserId(foodId, userId);
		if (isExistingFavorite) {
			Optional<FavoriteFood> favoriteFood = favoriteRepository.findByFoodFoodIdAndUserUserId(foodId, userId);
			favoriteFood.ifPresent(favoriteRepository::delete);
			throw new DuplicateFavoriteException("Duplicate favorite entry for foodId: " + foodId + " and userId: " + userId);
		}
		Optional<User> user = userRepository.findById(userId);
		Optional<Food> food = foodRepository.findById(foodId);
		if (user.isPresent() && food.isPresent()) {
			FavoriteFood favoriteFood = new FavoriteFood();
			favoriteFood.setUser(user.get());
			favoriteFood.setFood(food.get());
			favoriteRepository.save(favoriteFood);
			return true;
		}
		throw new NotFoundException("User or Food not found.");
	}

	public boolean removeFavorite(Long foodId, Long userId) {
		Optional<FavoriteFood> favoriteFood = favoriteRepository.findByFoodFoodIdAndUserUserId(foodId, userId);
		if (favoriteFood.isPresent()) {
			favoriteRepository.delete(favoriteFood.get());
			return true;
		}
		throw new NotFoundException("Favorite not found.");
	}
}

