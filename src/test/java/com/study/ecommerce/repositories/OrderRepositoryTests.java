package com.study.ecommerce.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.ecommerce.domain.Customer;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.domain.enums.OrderStatus;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @org.junit.jupiter.api.Order(1)
    void shouldFindOrderById() {
        Customer customer = customerRepository.save(new Customer(
            "Find Order Customer",
            "find.order@example.com",
            "+55 11 90000-1001"
        ));
        Order savedOrder = orderRepository.saveAndFlush(new Order(customer));

        Order foundOrder = orderRepository
            .findById(savedOrder.getId())
            .orElseThrow();

        assertEquals(savedOrder.getId(), foundOrder.getId());
        assertEquals(OrderStatus.PENDING_PAYMENT, foundOrder.getStatus());
        assertEquals("find.order@example.com", foundOrder.getCustomer().getEmail());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void shouldListAllOrders() {
        long quantityBeforeInsert = orderRepository.count();
        Customer firstCustomer = customerRepository.save(new Customer(
            "First Order Customer",
            "first.listed.order@example.com",
            "+55 11 90000-1002"
        ));
        Customer secondCustomer = customerRepository.save(new Customer(
            "Second Order Customer",
            "second.listed.order@example.com",
            "+55 11 90000-1003"
        ));
        Order firstOrder = orderRepository.saveAndFlush(new Order(firstCustomer));
        Order secondOrder = orderRepository.saveAndFlush(new Order(secondCustomer));

        List<Order> orders = orderRepository.findAll();

        assertEquals(quantityBeforeInsert + 2, orders.size());
        assertTrue(orders.stream()
            .anyMatch(order -> order.getId().equals(firstOrder.getId())));
        assertTrue(orders.stream()
            .anyMatch(order -> order.getId().equals(secondOrder.getId())));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void shouldInsertOrder() {
        Customer customer = customerRepository.save(new Customer(
            "Order Customer",
            "order.customer@example.com",
            "+55 11 96666-6666"
        ));
        Order order = new Order(customer);

        Order savedOrder = orderRepository.saveAndFlush(order);

        assertNotNull(savedOrder.getId());
        assertTrue(orderRepository.existsById(savedOrder.getId()));
        assertEquals(OrderStatus.PENDING_PAYMENT, savedOrder.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void shouldUpdateOrder() {
        Customer customer = customerRepository.save(new Customer(
            "Update Order Customer",
            "update.order@example.com",
            "+55 11 95555-5555"
        ));
        Order order = orderRepository.save(new Order(customer));
        Long orderId = order.getId();

        order.setStatus(OrderStatus.PAID);
        orderRepository.saveAndFlush(order);

        Order updatedOrder = orderRepository
            .findById(orderId)
            .orElseThrow();

        assertEquals(orderId, updatedOrder.getId());
        assertEquals(OrderStatus.PAID, updatedOrder.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void shouldDeleteOrderById() {
        Customer customer = customerRepository.save(new Customer(
            "Delete Order Customer",
            "delete.order@example.com",
            "+55 11 94444-4444"
        ));
        Order order = orderRepository.save(new Order(customer));

        Long orderId = order.getId();
        assertTrue(orderRepository.existsById(orderId));

        orderRepository.deleteById(orderId);

        assertFalse(orderRepository.existsById(orderId));
    }
}
