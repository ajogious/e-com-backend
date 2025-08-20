package com.ajogious.e_com_backend.services_impl;

import com.ajogious.e_com_backend.dtos.ProductRequest;
import com.ajogious.e_com_backend.dtos.ProductResponse;
import com.ajogious.e_com_backend.entities.Category;
import com.ajogious.e_com_backend.entities.Product;
import com.ajogious.e_com_backend.entities.ProductImage;
import com.ajogious.e_com_backend.repositories.CategoryRepository;
import com.ajogious.e_com_backend.repositories.ProductRepository;
import com.ajogious.e_com_backend.services.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CategoryRepository categoryRepo;

    private ProductResponse mapToResponse(Product product) {
        ProductResponse res = new ProductResponse();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setDescription(product.getDescription());
        res.setPrice(product.getPrice());
        res.setStock(product.getStock());
        res.setCategory(product.getCategory().getName());

        List<ProductResponse.ImageResponse> imageResponses = product.getImages().stream()
                .map(img -> {
                    ProductResponse.ImageResponse ir = new ProductResponse.ImageResponse();
                    ir.setId(img.getId());
                    ir.setUrl(img.getUrl());
                    ir.setAltText(img.getAltText());
                    return ir;
                }).toList();

        res.setImages(imageResponses);
        return res;
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size, String category, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Product> products;
        if (category != null) {
            products = productRepo.findByCategory_Name(category, pageable);
        } else if (search != null) {
            products = productRepo.findByNameContainingIgnoreCase(search, pageable);
        } else {
            products = productRepo.findAll(pageable);
        }

        return products.map(this::mapToResponse);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest req) {
        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(category);

        // Map image URLs to ProductImage entities
        List<ProductImage> images = req.getImageUrls().stream()
                .map(url -> {
                    ProductImage img = new ProductImage();
                    img.setUrl(url);
                    img.setAltText("Product image"); // optional
                    img.setProduct(product);
                    return img;
                }).toList();

        product.setImages(images);

        Product saved = productRepo.save(product);

        return mapToResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // update product fields
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(category);

        // Clear old images and replace with new ones
        product.getImages().clear();

        List<ProductImage> newImages = req.getImageUrls().stream()
                .map(url -> {
                    ProductImage img = new ProductImage();
                    img.setUrl(url);
                    img.setAltText("Product image"); // optional
                    img.setProduct(product);
                    return img;
                }).toList();

        product.getImages().addAll(newImages);

        Product updated = productRepo.save(product);

        return mapToResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepo.deleteById(id);
    }
}
