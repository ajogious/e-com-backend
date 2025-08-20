package com.ajogious.e_com_backend.controllers;

import com.ajogious.e_com_backend.dtos.ProductRequest;
import com.ajogious.e_com_backend.dtos.ProductResponse;
import com.ajogious.e_com_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET /products?page=0&size=10&category=Shoes&search=blue
    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        return productService.getAllProducts(page, size, category, search);
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // POST /products (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest req) {
        return productService.createProduct(req);
    }

    // PUT /products/{id} (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest req) {
        return productService.updateProduct(id, req);
    }

    // DELETE /products/{id} (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
