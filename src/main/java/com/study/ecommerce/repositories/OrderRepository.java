package com.study.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
