package com.foodshare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodshare.domain.Food;
import com.foodshare.service.FoodService;

@RestController
public class FoodApiController {
	@Autowired
	private FoodService foodService;

	@GetMapping("/foods")
	public ResponseEntity<List<Food>> getAllFoods() {
		List<Food> foods = foodService.findAll();
		return ResponseEntity.ok(foods);
	}

	@GetMapping("/foods/{id}")
	public ResponseEntity<Food> read(@PathVariable(value="id") Long foodId) {
		return foodService.read(foodId)
			.map(food -> ResponseEntity.ok(food))
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/foods")
	public ResponseEntity<Food> create(@RequestBody Food food) {
		Food createdFood = foodService.create(food);
		return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
	}

	@PutMapping("/foods/{id}")
	public ResponseEntity<Food> update(@PathVariable(value="id") Long foodId, @RequestBody Food food) {
		return foodService.read(foodId).map(existingFood -> {
			food.setFoodId(foodId); // Make sure the food ID is set correctly
			Food updatedFood = foodService.update(food);
			return new ResponseEntity<>(updatedFood, HttpStatus.OK);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/foods/{id}")
	public ResponseEntity<?> delete(@PathVariable(value="id") Long foodId) {
		return foodService.read(foodId).map(food -> {
			foodService.delete(foodId);
			return ResponseEntity.noContent().build(); // Successful deletion
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
