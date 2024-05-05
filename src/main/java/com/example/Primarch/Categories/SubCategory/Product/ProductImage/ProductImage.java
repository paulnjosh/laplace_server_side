package com.example.Primarch.Categories.SubCategory.Product.ProductImage;

import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;
    private String filePath;
    private String description;

    @Transient
    public String imageUrl;
//    @Transient
//    @JsonIgnore
//    @Value("${serverIp}")
//    public String serverIp;

    private Date createdOn;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_fk")
    @JsonIgnore
    private Product product;

    public String getImageUrl(){
        return "http://localhost:9090/image-server/images/"+name;
    }
}
