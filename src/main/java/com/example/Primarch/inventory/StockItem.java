package com.example.Primarch.inventory;

import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    @ManyToOne
    @JsonIgnore
    private Product product;
    private Double cost;

    private Date stockFor; // date stock was bought
    private Date createdOn;
    private Date modifiedOn;
}
