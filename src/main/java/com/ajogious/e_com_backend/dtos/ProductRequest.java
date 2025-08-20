package com.ajogious.e_com_backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private List<String> imageUrls;

}
