package com.study.ecommerce.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.ecommerce.domain.Customer;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.OrderItem;
import com.study.ecommerce.domain.OrderItemId;
import com.study.ecommerce.domain.Product;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderItemRepositoryTests {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @org.junit.jupiter.api.Order(1)
    void shouldFindOrderItemById() {
        Order order = createOrder("find.item@example.com");
        Product product = createProduct("Product for item search");
        OrderItem savedOrderItem = orderItemRepository.saveAndFlush(
            new OrderItem(order, product, 2)
        );

        OrderItem foundOrderItem = orderItemRepository
            .findById(savedOrderItem.getId())
            .orElseThrow();

        assertEquals(savedOrderItem.getId(), foundOrderItem.getId());
        assertEquals(order.getId(), foundOrderItem.getOrder().getId());
        assertEquals(product.getId(), foundOrderItem.getProduct().getId());
        assertEquals(2, foundOrderItem.getQuantity());
        assertEquals(new BigDecimal("25.00"), foundOrderItem.getUnitPrice());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void shouldListAllOrderItems() {
        long quantityBeforeInsert = orderItemRepository.count();
        Order firstOrder = createOrder("first.listed.item@example.com");
        Order secondOrder = createOrder("second.listed.item@example.com");
        Product firstProduct = createProduct("First product for item list");
        Product secondProduct = createProduct("Second product for item list");
        OrderItem firstItem = orderItemRepository.saveAndFlush(
            new OrderItem(firstOrder, firstProduct, 1)
        );
        OrderItem secondItem = orderItemRepository.saveAndFlush(
            new OrderItem(secondOrder, secondProduct, 2)
        );

        List<OrderItem> orderItems = orderItemRepository.findAll();

        assertEquals(quantityBeforeInsert + 2, orderItems.size());
        assertTrue(orderItems.stream()
            .anyMatch(item -> item.getId().equals(firstItem.getId())));
        assertTrue(orderItems.stream()
            .anyMatch(item -> item.getId().equals(secondItem.getId())));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void shouldInsertOrderItem() {
        Order order = createOrder("insert.item@example.com");
        Product product = createProduct("Product for new item");
        OrderItem orderItem = new OrderItem(order, product, 2);

        OrderItem savedOrderItem = orderItemRepository.saveAndFlush(orderItem);

        assertNotNull(savedOrderItem.getId());
        assertTrue(orderItemRepository.existsById(savedOrderItem.getId()));
        assertEquals(2, savedOrderItem.getQuantity());
        assertEquals(order.getId(), savedOrderItem.getOrder().getId());
        assertEquals(product.getId(), savedOrderItem.getProduct().getId());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void shouldUpdateOrderItem() {
        Order order = createOrder("update.item@example.com");
        Product product = createProduct("Product for updated item");
        OrderItem orderItem = orderItemRepository.save(new OrderItem(order, product, 1));
        OrderItemId orderItemId = orderItem.getId();

        orderItem.setQuantity(3);
        orderItemRepository.saveAndFlush(orderItem);

        OrderItem updatedOrderItem = orderItemRepository
            .findById(orderItemId)
            .orElseThrow();

        assertEquals(orderItemId, updatedOrderItem.getId());
        assertEquals(3, updatedOrderItem.getQuantity());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void shouldDeleteOrderItemById() {
        Order order = createOrder("delete.item@example.com");
        Product product = createProduct("Product for deleted item");
        OrderItem orderItem = orderItemRepository.save(new OrderItem(order, product, 1));

        OrderItemId orderItemId = orderItem.getId();
        assertTrue(orderItemRepository.existsById(orderItemId));

        orderItemRepository.deleteById(orderItemId);

        assertFalse(orderItemRepository.existsById(orderItemId));
    }

    private Order createOrder(String customerEmail) {
        Customer customer = customerRepository.save(new Customer(
            "Order Item Customer",
            customerEmail,
            "+55 11 92222-2222"
        ));

        return orderRepository.save(new Order(customer));
    }

    private Product createProduct(String name) {
        Product product = new Product(
            name,
            "Product used in an order item test",
            new BigDecimal("25.00")
        );

        return productRepository.save(product);
    }
}
