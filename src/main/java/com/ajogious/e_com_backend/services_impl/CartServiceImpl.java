package com.ajogious.e_com_backend.services_impl;

import com.ajogious.e_com_backend.dtos.AddToCartRequest;
import com.ajogious.e_com_backend.dtos.CartItemResponse;
import com.ajogious.e_com_backend.dtos.CartResponse;
import com.ajogious.e_com_backend.entities.CartItem;
import com.ajogious.e_com_backend.entities.Product;
import com.ajogious.e_com_backend.entities.User;
import com.ajogious.e_com_backend.repositories.CartItemRepository;
import com.ajogious.e_com_backend.repositories.ProductRepository;
import com.ajogious.e_com_backend.services.CartService;
import com.ajogious.e_com_backend.utils.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;
    private final AuthUtil authUtil;

    public CartServiceImpl(CartItemRepository cartItemRepo, ProductRepository productRepo, AuthUtil authUtil) {
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
        this.authUtil = authUtil;
    }

    @Override
    public CartResponse getCartForCurrentUser() {
        User user = authUtil.getCurrentUser();
        List<CartItem> items = cartItemRepo.findByCustomer(user);

        List<CartItemResponse> responses = items.stream().map(this::mapToResponse).collect(Collectors.toList());

        double total = responses.stream().mapToDouble(CartItemResponse::getTotal).sum();

        CartResponse cart = new CartResponse();
        cart.setItems(responses);
        cart.setTotalPrice(total);
        return cart;
    }

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        User user = authUtil.getCurrentUser();
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // check if item already in cart
        CartItem item = cartItemRepo.findByCustomerAndProduct(user, product)
                .orElse(new CartItem(user, product, 0));

        item.setQuantity(item.getQuantity() + request.getQuantity());
        cartItemRepo.save(item);

        return getCartForCurrentUser();
    }

    @Override
    public void removeFromCart(Long cartItemId) {
        User user = authUtil.getCurrentUser();
        CartItem item = cartItemRepo.findByIdAndCustomer(cartItemId, user)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepo.delete(item);
    }

    @Override
    public void clearCartForCurrentUser() {
        User user = authUtil.getCurrentUser();
        cartItemRepo.deleteByCustomer(user);
    }

    private CartItemResponse mapToResponse(CartItem item) {
        CartItemResponse resp = new CartItemResponse();
        resp.setId(item.getId());
        resp.setProductId(item.getProduct().getId());
        resp.setProductName(item.getProduct().getName());
        resp.setPrice(item.getProduct().getPrice());
        resp.setQuantity(item.getQuantity());
        resp.setTotal(item.getQuantity() * item.getProduct().getPrice());
        return resp;
    }
}
