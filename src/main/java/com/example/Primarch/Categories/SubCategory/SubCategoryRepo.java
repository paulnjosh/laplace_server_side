package com.example.Primarch.Categories.SubCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findBySubCategoryName(String name);
}
