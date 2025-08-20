package com.ajogious.e_com_backend.dtos;

import lombok.*;

import java.util.List;

@Data
public class CartResponse {
    private List<CartItemResponse> items;
    private double totalPrice;
}
