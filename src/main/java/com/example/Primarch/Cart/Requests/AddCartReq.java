package com.example.Primarch.Cart.Requests;

import lombok.Data;

import java.util.List;

@Data
public class AddCartReq {
    private String userEmail;
    private Double totalDiscount;
    private Double amtPaidCash;
    private Double amtPaidMpesa;
    private List<ProdCartHolder> products;
}
