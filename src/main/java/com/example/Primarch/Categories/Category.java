package com.example.Primarch.Categories;

import com.example.Primarch.Categories.SubCategory.SubCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //details for category
    private String categoryName;
    private String description;
    private String catIcon;
    private String seoInformation; //Meta title, meta description, and keywords for search engine optimization
//    private String parentCat;

    private String status; // Indicate whether the category, sub-category, or brand is active or inactive.
    private String sortOrder; //  Establish a sequence for how categories, sub-categories, or brands appear on the website.
    private String visibility; //Choose whether to display the category, sub-category, or brand on the website's frontend.

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<SubCategory> subCategories= new ArrayList<>();
}
