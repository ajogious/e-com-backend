package com.ajogious.e_com_backend.controllers;

import com.ajogious.e_com_backend.dtos.AddToCartRequest;
import com.ajogious.e_com_backend.dtos.CartResponse;
import com.ajogious.e_com_backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add item to cart
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    // View cart
    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CartResponse> viewCart() {
        return ResponseEntity.ok(cartService.getCartForCurrentUser());
    }

    // Remove item from cart
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> removeItem(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return ResponseEntity.ok("Item removed from cart");
    }
}
