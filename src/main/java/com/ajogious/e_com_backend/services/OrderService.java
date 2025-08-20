package com.ajogious.e_com_backend.services;

import com.ajogious.e_com_backend.dtos.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrderFromCart();

    List<OrderResponse> getOrdersForCurrentUser();

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(Long id, String status); // admin only
}
