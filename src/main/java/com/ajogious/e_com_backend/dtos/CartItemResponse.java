package com.ajogious.e_com_backend.dtos;

import lombok.*;

@Data
public class CartItemResponse {
    private Long id; // cart item id
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private double total; // price * quantity
}
