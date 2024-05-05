package com.example.Primarch.Categories.SubCategory;

import com.example.Primarch.Categories.Category;
import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_category")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subCategoryName;
    private String description;
    private String icon;
    private String seoInfo;

    private String status; // Indicate whether the category, sub-category, or brand is active or inactive.
    private String sortOrder; //  Establish a sequence for how categories, sub-categories, or brands appear on the website.
    private String visibility; //Choose whether to display the category, sub-category, or brand on the website's frontend.

    @Transient
    public String categoryName;

    @Transient
    public Long catId;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products= new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_fk")
    @JsonIgnore
    private Category category;

    public String getCategoryName(){
        if(this.category !=null){
            return this.category.getCategoryName();
        }
        return null;
    }

    public Long getCatId(){
        if(this.category !=null){
            return this.category.getId();
        }
        return null;
    }
}
