package com.example.Primarch.ProductUiClassification;

import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductUiClassification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String classificationName;
    @OneToMany(mappedBy = "productUiClassification",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
}
