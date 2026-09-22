package com.study.ecommerce.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.ecommerce.domain.Category;
import com.study.ecommerce.domain.Product;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    void shouldFindProductById() {
        Product savedProduct = productRepository.saveAndFlush(createProductWithCategory(
            "Product to find",
            "Description of the product to find",
            "79.90"
        ));

        Product foundProduct = productRepository
            .findById(savedProduct.getId())
            .orElseThrow();

        assertEquals(savedProduct.getId(), foundProduct.getId());
        assertEquals("Product to find", foundProduct.getName());
        assertEquals(new BigDecimal("79.90"), foundProduct.getPrice());
    }

    @Test
    @Order(2)
    void shouldListAllProducts() {
        long quantityBeforeInsert = productRepository.count();
        productRepository.saveAndFlush(createProductWithCategory(
            "First listed product",
            "First product description",
            "10.00"
        ));
        productRepository.saveAndFlush(createProductWithCategory(
            "Second listed product",
            "Second product description",
            "20.00"
        ));

        List<Product> products = productRepository.findAll();

        assertEquals(quantityBeforeInsert + 2, products.size());
        assertTrue(products.stream()
            .anyMatch(product -> product.getName().equals("First listed product")));
        assertTrue(products.stream()
            .anyMatch(product -> product.getName().equals("Second listed product")));
    }

    @Test
    @Order(3)
    void shouldInsertProduct() {
        Product product = createProductWithCategory(
            "New Product",
            "Description of the new product",
            "99.90"
        );

        Product savedProduct = productRepository.saveAndFlush(product);

        assertNotNull(savedProduct.getId());
        assertTrue(productRepository.existsById(savedProduct.getId()));
        assertEquals("New Product", savedProduct.getName());
        assertFalse(savedProduct.getCategories().isEmpty());
    }

    @Test
    @Order(4)
    void shouldUpdateProduct() {
        Product product = productRepository.save(createProductWithCategory(
            "Old Product",
            "Old description",
            "49.90"
        ));
        Long productId = product.getId();

        product.setName("Updated Product");
        product.setPrice(new BigDecimal("59.90"));
        productRepository.saveAndFlush(product);

        Product updatedProduct = productRepository
            .findById(productId)
            .orElseThrow();

        assertEquals(productId, updatedProduct.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(new BigDecimal("59.90"), updatedProduct.getPrice());
    }

    @Test
    @Order(5)
    void shouldDeleteProductById() {
        Product product = productRepository.save(createProductWithCategory(
            "Product to delete",
            "This product will be deleted",
            "29.90"
        ));

        Long productId = product.getId();
        assertTrue(productRepository.existsById(productId));

        productRepository.deleteById(productId);

        assertFalse(productRepository.existsById(productId));
    }

    private Product createProductWithCategory(
        String name,
        String description,
        String price
    ) {
        Category category = categoryRepository
            .findById(1L)
            .orElseThrow();

        Product product = new Product(name, description, new BigDecimal(price));
        product.getCategories().add(category);

        return product;
    }
}
