package com.foodshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodshare.domain.FoodImage;
@Repository
public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {
}
