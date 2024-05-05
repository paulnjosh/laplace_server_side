package com.example.Primarch.Categories.SubCategory.Product;
import com.example.Primarch.ProductUiClassification.ProductUiClassification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByProductUiClassification(ProductUiClassification productUiClassification);


    @Query(nativeQuery = true,value = "SELECT * FROM `product` WHERE `availability` LIKE %:searchString% OR `description` LIKE %:searchString% OR `name` LIKE %:searchString% OR `price` LIKE %:searchString% OR `sku`LIKE %:searchString% OR `status` LIKE %:searchString% OR `wholesale_price` LIKE %:searchString% OR `retail_price` LIKE %:searchString%")
    Page<Product> searchAll(Pageable paging,String searchString);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE `product` SET `qty`=qty+:qty WHERE `id`=:productId")
    void updateSTock(Long productId, Integer qty);
}
