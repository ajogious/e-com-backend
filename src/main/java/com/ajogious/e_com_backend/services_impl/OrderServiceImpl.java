package com.ajogious.e_com_backend.services_impl;

import com.ajogious.e_com_backend.dtos.OrderItemResponse;
import com.ajogious.e_com_backend.dtos.OrderResponse;
import com.ajogious.e_com_backend.entities.*;
import com.ajogious.e_com_backend.repositories.CartItemRepository;
import com.ajogious.e_com_backend.repositories.OrderRepository;
import com.ajogious.e_com_backend.repositories.OrderItemRepository;
import com.ajogious.e_com_backend.services.OrderService;
import com.ajogious.e_com_backend.utils.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartItemRepository cartItemRepo;
    private final AuthUtil authUtil;

    public OrderServiceImpl(OrderRepository orderRepo, OrderItemRepository orderItemRepo,
            CartItemRepository cartItemRepo, AuthUtil authUtil) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.cartItemRepo = cartItemRepo;
        this.authUtil = authUtil;
    }

    @Override
    public OrderResponse createOrderFromCart() {
        User user = authUtil.getCurrentUser();
        List<CartItem> cartItems = cartItemRepo.findByCustomer(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setCustomer(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());
                    return orderItem;
                })
                .toList();

        order.setItems(orderItems);

        order.setTotalAmount(
                orderItems.stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum());

        // ✅ Save and use the saved entity
        Order savedOrder = orderRepo.save(order);

        // Clear the cart
        cartItemRepo.deleteAll(cartItems);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersForCurrentUser() {
        User user = authUtil.getCurrentUser();
        return orderRepo.findByCustomer(user).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderRepo.save(order);
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse resp = new OrderResponse();
        resp.setId(order.getId());
        resp.setStatus(order.getStatus().name());
        resp.setTotalAmount(order.getTotalAmount());
        resp.setCreatedAt(order.getCreatedAt()); // ✅ FIX: include createdAt

        List<OrderItemResponse> items = order.getItems().stream().map(oi -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(oi.getProduct().getId());
            r.setProductName(oi.getProduct().getName());
            r.setQuantity(oi.getQuantity());
            r.setPrice(oi.getProduct().getPrice());
            r.setTotal(oi.getQuantity() * oi.getProduct().getPrice());
            return r;
        }).collect(Collectors.toList());

        resp.setItems(items);
        return resp;
    }

}
