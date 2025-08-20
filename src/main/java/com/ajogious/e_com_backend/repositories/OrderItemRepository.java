package com.ajogious.e_com_backend.repositories;

import com.ajogious.e_com_backend.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
