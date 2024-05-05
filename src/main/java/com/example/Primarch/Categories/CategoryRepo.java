package com.example.Primarch.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    Optional<Category> findByCategoryName(String catName);

    Boolean existsByCategoryName(String catName);

//    List<Category>findByAllCategoryName(String catName);

    @Query(nativeQuery = true,value = "SELECT * FROM `category` WHERE `category_name` LIKE %:searchString% OR `description` LIKE %:searchString% OR `seo_information` LIKE %:searchString% OR `status` LIKE %:searchString%")
    List<Category> searchAll(String searchString);
}
