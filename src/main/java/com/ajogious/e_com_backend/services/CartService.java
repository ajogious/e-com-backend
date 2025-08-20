package com.ajogious.e_com_backend.services;

import com.ajogious.e_com_backend.dtos.AddToCartRequest;
import com.ajogious.e_com_backend.dtos.CartResponse;

public interface CartService {
    CartResponse getCartForCurrentUser();

    CartResponse addToCart(AddToCartRequest request);

    void removeFromCart(Long cartItemId);

    void clearCartForCurrentUser();
}
