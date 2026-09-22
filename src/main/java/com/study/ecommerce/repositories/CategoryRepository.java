package com.study.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
