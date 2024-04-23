package com.foodshare.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodshare.domain.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	List<Food> findAll();

	boolean existsByCategoryCategoryId(Long categoryId);
}
