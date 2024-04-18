package com.foodshare.board.mapper;

import com.foodshare.board.dto.FoodDTO;
import com.foodshare.domain.Category;
import com.foodshare.domain.Food;
import com.foodshare.domain.FoodImage;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T14:50:38+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public FoodDTO convertToFoodDTO(Food food, List<FoodImage> foodImages, Category category) {
        if ( food == null && foodImages == null && category == null ) {
            return null;
        }

        FoodDTO foodDTO = new FoodDTO();

        if ( food != null ) {
            foodDTO.setFoodId( food.getFoodId() );
            foodDTO.setMakeByDate( timestampToLocalDate( food.getMakeByDate() ) );
            foodDTO.setEatByDate( timestampToLocalDate( food.getEatByDate() ) );
            foodDTO.setTitle( food.getTitle() );
            foodDTO.setDescription( food.getDescription() );
            foodDTO.setStatus( food.getStatus() );
        }
        if ( category != null ) {
            foodDTO.setCategory( category.getName() );
        }
        foodDTO.setImageUrls( mapImagePaths(foodImages) );

        return foodDTO;
    }

    @Override
    public Food convertToFood(FoodDTO foodDTO) {
        if ( foodDTO == null ) {
            return null;
        }

        Food food = new Food();

        food.setMakeByDate( localDateToTimestamp( foodDTO.getMakeByDate() ) );
        food.setEatByDate( localDateToTimestamp( foodDTO.getEatByDate() ) );
        food.setStatus( foodDTO.getStatus() );
        food.setTitle( foodDTO.getTitle() );
        food.setDescription( foodDTO.getDescription() );

        food.setViews( 0 );
        food.setLikes( 0 );
        food.setCreatedAt( java.sql.Timestamp.from(java.time.Instant.now()) );
        food.setUpdatedAt( java.sql.Timestamp.from(java.time.Instant.now()) );

        return food;
    }
}
