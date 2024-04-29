package com.foodshare.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodshare.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
