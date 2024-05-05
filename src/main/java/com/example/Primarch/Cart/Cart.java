package com.example.Primarch.Cart;

import com.example.Primarch.Cart.CartItem.CartItem;
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
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double subTotal;
    private Double discountTotal;
    private Double amtPaidCash;
    private Double amtPaidMpesa;

    private String cartStatus; //pending, processed, closed

    private Date createdOn;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems=new ArrayList<>();

    public void setCartItems(List<CartItem> cartItems){
        if(cartItems!=null){
            cartItems.forEach(cartItem -> {
                cartItem.setCart(this);
            });
            this.cartItems=cartItems;
        }
    }
}
