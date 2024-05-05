package com.example.Primarch.Brands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String brandName;
    private String description;
    private String icon;
    private String seoInformation;
    private String countryOfOrigin;
    private String websiteLink;

    private String status; // Indicate whether the category, sub-category, or brand is active or inactive.
    private String sortOrder; //  Establish a sequence for how categories, sub-categories, or brands appear on the website.
    private String visibility; //Choose whether to display the category, sub-category, or brand on the website's frontend.
}
