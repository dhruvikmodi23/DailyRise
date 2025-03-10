package com.example.repository;

import com.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> { // Entity and primary key data type
    Category findByNameAndUserId(String categoryName, Long userId);
    Category findByIdAndUserId(Long categoryId, Long userId);
}
