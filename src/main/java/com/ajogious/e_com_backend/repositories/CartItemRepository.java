package com.ajogious.e_com_backend.repositories;

import com.ajogious.e_com_backend.entities.CartItem;
import com.ajogious.e_com_backend.entities.Product;
import com.ajogious.e_com_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(User customer);

    Optional<CartItem> findByCustomerAndProduct(User customer, Product product);

    Optional<CartItem> findByIdAndCustomer(Long id, User customer);

    void deleteByCustomer(User customer);
}
