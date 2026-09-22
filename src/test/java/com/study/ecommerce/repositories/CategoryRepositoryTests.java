package com.study.ecommerce.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.ecommerce.domain.Category;

import jakarta.transaction.Transactional;

@SpringBootTest 
@Transactional 
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryRepositoryTests {
    @Autowired 
    private CategoryRepository categoryRepository;

    @Test 
    @Order(1)
    void shouldFindCategoryById() {
        Category savedCategory = categoryRepository.saveAndFlush(new Category("Category to find"));

        Category foundCategory = categoryRepository
            .findById(savedCategory.getId())
            .orElseThrow();

        assertEquals(savedCategory.getId(), foundCategory.getId());
        assertEquals("Category to find", foundCategory.getName());
    }

    @Test 
    @Order(2)
    void shouldListAllCategories() {
        long quantityBeforeInsert = categoryRepository.count();
        categoryRepository.saveAndFlush(new Category("First listed category"));
        categoryRepository.saveAndFlush(new Category("Second listed category"));

        List<Category> categories = categoryRepository.findAll();

        assertEquals(quantityBeforeInsert + 2, categories.size());
        assertTrue(categories.stream()
            .anyMatch(category -> category.getName().equals("First listed category")));
        assertTrue(categories.stream()
            .anyMatch(category -> category.getName().equals("Second listed category")));
    }

    @Test
    @Order(3)
    void shouldInsertCategory() {
        Category category = new Category("New category");

        Category savedCategory = categoryRepository.saveAndFlush(category);

        assertNotNull(savedCategory.getId());
        assertTrue(categoryRepository.existsById(savedCategory.getId()));

        Category foundCategory = categoryRepository
            .findById(savedCategory.getId())
            .orElseThrow();

        assertEquals("New category", foundCategory.getName());
    }

    @Test 
    @Order(4)
    void shouldUpdateCategoryById() {
        Category category = categoryRepository.save(new Category("Old category name"));
        Long categoryId = category.getId();

        category.setName("Updated category name");
        categoryRepository.saveAndFlush(category);

        Category updatedCategory = categoryRepository
            .findById(categoryId)
            .orElseThrow();

        assertEquals(categoryId, updatedCategory.getId());
        assertEquals("Updated category name", updatedCategory.getName());
    }

    @Test 
    @Order(5)
    void shouldDeleteCategoryById() {
        Category category = categoryRepository.save(new Category("Category to delete"));

        Long categoryId = category.getId();
        assertTrue(categoryRepository.existsById(categoryId));

        categoryRepository.deleteById(categoryId);

        assertFalse(categoryRepository.existsById(categoryId));
    }
} 
