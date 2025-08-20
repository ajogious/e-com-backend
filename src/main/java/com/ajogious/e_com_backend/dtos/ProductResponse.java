package com.ajogious.e_com_backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;

    private List<ImageResponse> images;

    @Data
    public static class ImageResponse {
        private Long id;
        private String url;
        private String altText;
    }

}