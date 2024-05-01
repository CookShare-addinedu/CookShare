package com.foodshare.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodshare.domain.FavoriteFood;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {

	FavoriteFood findByFoodFoodIdAndUserUserId(Long foodId, Long userId);

	long countByFoodFoodId(Long foodId);
}
