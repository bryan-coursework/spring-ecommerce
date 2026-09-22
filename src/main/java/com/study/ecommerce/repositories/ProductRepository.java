package com.study.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
