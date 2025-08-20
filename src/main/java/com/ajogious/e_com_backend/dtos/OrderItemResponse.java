package com.ajogious.e_com_backend.dtos;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double total; // ✅ added
}
