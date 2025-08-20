package com.ajogious.e_com_backend.repositories;

import com.ajogious.e_com_backend.entities.Order;
import com.ajogious.e_com_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer); // ✅ matches entity
}
