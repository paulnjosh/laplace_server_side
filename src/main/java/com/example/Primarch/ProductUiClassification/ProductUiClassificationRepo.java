package com.example.Primarch.ProductUiClassification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductUiClassificationRepo extends JpaRepository<ProductUiClassification,Long> {
    Optional<ProductUiClassification> findByClassificationName(String classificationName);
}
