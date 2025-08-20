package com.ajogious.e_com_backend.dtos;

import lombok.*;

@Data
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}
