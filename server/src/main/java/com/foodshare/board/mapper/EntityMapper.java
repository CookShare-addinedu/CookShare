package com.foodshare.board.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.foodshare.domain.Category;
import com.foodshare.domain.FavoriteFood;
import com.foodshare.domain.Food;
import com.foodshare.domain.FoodImage;
import com.foodshare.board.dto.FoodDTO;


@Mapper(componentModel = "spring")
public interface EntityMapper {

	@Mappings({
		@Mapping(target = "foodId", source = "food.foodId"),
		@Mapping(target = "imageUrls", expression = "java(mapImagePaths(foodImages))"),
		@Mapping(target = "category", source = "category.name"),
		@Mapping(target = "makeByDate", source = "food.makeByDate", qualifiedByName = "timestampToLocalDate"),
		@Mapping(target = "eatByDate", source = "food.eatByDate", qualifiedByName = "timestampToLocalDate"),
		@Mapping(target = "createdAt", source = "food.createdAt", qualifiedByName = "timestampToLocalDate"),
		@Mapping(target = "title", source = "food.title"),
		@Mapping(target = "description", source = "food.description"),
		@Mapping(target = "giver", source = "food.giver"),
		@Mapping(target = "location", source = "food.location"),  // 직접 지정 또는 자동 추출
		@Mapping(target = "likes", source = "food.likes"),
		@Mapping(target = "isFavorite", source = "favoritefood.isFavorite"),
	})
	FoodDTO convertToFoodDTO(Food food, List<FoodImage> foodImages, Category category, FavoriteFood favoritefood);

	default List<String> mapImagePaths(List<FoodImage> foodImages) {
		if (foodImages == null) return Collections.emptyList();
		return foodImages.stream()
			.flatMap(image -> image.getImagePaths().stream())
			.collect(Collectors.toList());
	}

	@Named("timestampToLocalDate")
	default LocalDate timestampToLocalDate(Timestamp timestamp) {
		return timestamp != null ? timestamp.toLocalDateTime().toLocalDate() : null;
	}

	@Mapping(target = "foodId", ignore = true)
	@Mapping(target = "location", source = "foodDTO.location")
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "giver", source = "foodDTO.giver")
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

	@Named("convertToFoodImage")
	default FoodImage convertToFoodImage(FoodDTO foodDTO, Food food) {
		if (foodDTO.getImageUrls() == null) return null;
		FoodImage foodImage = FoodImage.builder()
			.imagePaths(foodDTO.getImageUrls())
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

	@Named("converToFavoriteFood")
	default FavoriteFood convertToFavoriteFood(FoodDTO foodDTO, Food food) {
		FavoriteFood favoriteFood = FavoriteFood.builder()
			.isFavorite(foodDTO.getIsFavorite())
			.user(foodDTO.getGiver())
			.food(food)
			.build();
		return favoriteFood;
	}
}