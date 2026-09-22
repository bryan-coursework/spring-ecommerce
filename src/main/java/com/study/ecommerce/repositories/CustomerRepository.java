package com.study.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
