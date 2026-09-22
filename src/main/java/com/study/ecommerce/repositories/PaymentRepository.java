package com.study.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
