package com.study.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.OrderItem;
import com.study.ecommerce.domain.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
