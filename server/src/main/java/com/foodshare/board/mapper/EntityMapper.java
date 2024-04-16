package com.foodshare.board.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import com.foodshare.domain.Category;
import com.foodshare.domain.Food;
import com.foodshare.domain.FoodImage;
import com.foodshare.board.dto.FoodDTO;

@Mapper(componentModel = "spring")
public interface EntityMapper {

	@Mapping(target = "foodId", ignore = true)
	@Mapping(target = "location", ignore = true)
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "giver", ignore = true)
	@Mapping(target = "receiver", ignore = true)
	@Mapping(target = "views", constant = "0")
	@Mapping(target = "likes", constant = "0")
	@Mapping(target = "makeByDate", source = "makeByDate", qualifiedByName = "localDateToTimestamp")
	@Mapping(target = "eatByDate", source = "eatByDate", qualifiedByName = "localDateToTimestamp")
	@Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
	@Mapping(target = "updatedAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
	Food convertToFood(FoodDTO foodDTO);

	@Named("localDateToTimestamp")
	default Timestamp localDateToTimestamp(LocalDate date) {
		return date == null ? null : Timestamp.valueOf(date.atStartOfDay());
	}

	default FoodImage convertToFoodImage(FoodDTO foodDTO, Food food) {
		if (foodDTO.getImageUri() == null)
			return null;
		FoodImage foodImage = FoodImage.builder()
			.imagePath(foodDTO.getImageUri())
			.food(food)
			.createdAt(Timestamp.from(Instant.now()))
			.updatedAt(Timestamp.from(Instant.now()))
			.build();
		return foodImage;
	}

	@Named("convertToCategory")
	default Category convertToCategory(String categoryName) {
		Category category = Category.builder()
			.name(categoryName)
			.createdAt(Timestamp.from(Instant.now()))
			.updatedAt(Timestamp.from(Instant.now()))
			.build();
		return category;
	}
}