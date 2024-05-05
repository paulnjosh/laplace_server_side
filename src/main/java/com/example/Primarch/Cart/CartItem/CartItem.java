package com.example.Primarch.Cart.CartItem;

import com.example.Primarch.Cart.Cart;
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
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    private Date addedOn;
    @JsonIgnore
    private Double retailPrice;
    @JsonIgnore
    private Double wholesalePrice;

    @Transient
    public Product productItem;


    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_fk")
    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_fk")
    @JsonIgnore
    private Cart cart;

//    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_profile_fk")
//    @JsonIgnore
//    private UserProfile userProfile;

//    public Product getProductItem(){
//        if(this.product !=null){
//            return this.product;
//        }
//        return null;
//    }

}
