package com.ajogious.e_com_backend.services;

import com.ajogious.e_com_backend.dtos.ProductRequest;
import com.ajogious.e_com_backend.dtos.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductResponse> getAllProducts(int page, int size, String category, String search);

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
