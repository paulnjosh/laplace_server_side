package com.example.Primarch.Categories.SubCategory.Product;

import com.example.Primarch.Cart.CartItem.CartItem;
import com.example.Primarch.Categories.SubCategory.Product.ProductImage.ProductImage;
import com.example.Primarch.Categories.SubCategory.SubCategory;
import com.example.Primarch.ProductUiClassification.ProductUiClassification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //details for product
    private String name;
    private String description;
    private String sku; //(Stock Keeping Unit): Unique identifier for internal tracking.
    private Double retailPrice;
    private Double wholesalePrice;
    private Integer qty;
    private String currency;
    private String availability;
    private String productSize; //e.g 400ml
    private String color;

    private String status; // Indicate whether the category, sub-category, or brand is active or inactive.
    private String sortOrder; //  Establish a sequence for how categories, sub-categories, or brands appear on the website.
    private String visibility; //Choose whether to display the category, sub-category, or brand on the website's frontend.
    private String photoLink;

    private Date createdOn;

    @Transient
    public Long subCatId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> cartItems= new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductImage productImage;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_cat_cat_fk")
    @JsonIgnore
    private SubCategory subCategory;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "ui_classification_fk")
    @JsonIgnore
    private ProductUiClassification productUiClassification;

    public void setProductImage(ProductImage productImage){
        if(productImage !=null){
            productImage.setProduct(this);
            this.productImage=productImage;
        }
    }

    public  Long getSubCatId(){
        return this.subCategory.getId();
    }
}
