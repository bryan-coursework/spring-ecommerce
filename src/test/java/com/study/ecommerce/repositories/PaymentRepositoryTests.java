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
import com.study.ecommerce.domain.Payment;
import com.study.ecommerce.domain.enums.PaymentMethod;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentRepositoryTests {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @org.junit.jupiter.api.Order(1)
    void shouldFindPaymentById() {
        Order order = createOrder("find.payment@example.com");
        Payment savedPayment = paymentRepository.saveAndFlush(new Payment(
            order,
            PaymentMethod.PIX,
            new BigDecimal("75.00")
        ));

        Payment foundPayment = paymentRepository
            .findById(savedPayment.getId())
            .orElseThrow();

        assertEquals(savedPayment.getId(), foundPayment.getId());
        assertEquals(PaymentMethod.PIX, foundPayment.getMethod());
        assertEquals(new BigDecimal("75.00"), foundPayment.getAmount());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void shouldListAllPayments() {
        long quantityBeforeInsert = paymentRepository.count();
        Order firstOrder = createOrder("first.listed.payment@example.com");
        Order secondOrder = createOrder("second.listed.payment@example.com");
        Payment firstPayment = paymentRepository.saveAndFlush(new Payment(
            firstOrder,
            PaymentMethod.PIX,
            new BigDecimal("50.00")
        ));
        Payment secondPayment = paymentRepository.saveAndFlush(new Payment(
            secondOrder,
            PaymentMethod.CREDIT_CARD,
            new BigDecimal("80.00")
        ));

        List<Payment> payments = paymentRepository.findAll();

        assertEquals(quantityBeforeInsert + 2, payments.size());
        assertTrue(payments.stream()
            .anyMatch(payment -> payment.getId().equals(firstPayment.getId())));
        assertTrue(payments.stream()
            .anyMatch(payment -> payment.getId().equals(secondPayment.getId())));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void shouldInsertPayment() {
        Order order = createOrder("insert.payment@example.com");
        Payment payment = new Payment(
            order,
            PaymentMethod.PIX,
            new BigDecimal("100.00")
        );

        Payment savedPayment = paymentRepository.saveAndFlush(payment);

        assertNotNull(savedPayment.getId());
        assertTrue(paymentRepository.existsById(savedPayment.getId()));
        assertEquals(PaymentMethod.PIX, savedPayment.getMethod());
        assertEquals(order.getId(), savedPayment.getOrder().getId());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void shouldUpdatePayment() {
        Order order = createOrder("update.payment@example.com");
        Payment payment = paymentRepository.save(new Payment(
            order,
            PaymentMethod.PIX,
            new BigDecimal("100.00")
        ));
        Long paymentId = payment.getId();

        payment.setMethod(PaymentMethod.CREDIT_CARD);
        paymentRepository.saveAndFlush(payment);

        Payment updatedPayment = paymentRepository
            .findById(paymentId)
            .orElseThrow();

        assertEquals(paymentId, updatedPayment.getId());
        assertEquals(PaymentMethod.CREDIT_CARD, updatedPayment.getMethod());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void shouldDeletePaymentById() {
        Order order = createOrder("delete.payment@example.com");
        Payment payment = paymentRepository.save(new Payment(
            order,
            PaymentMethod.PIX,
            new BigDecimal("100.00")
        ));

        Long paymentId = payment.getId();
        assertTrue(paymentRepository.existsById(paymentId));

        paymentRepository.deleteById(paymentId);

        assertFalse(paymentRepository.existsById(paymentId));
    }

    private Order createOrder(String customerEmail) {
        Customer customer = customerRepository.save(new Customer(
            "Payment Customer",
            customerEmail,
            "+55 11 93333-3333"
        ));

        return orderRepository.save(new Order(customer));
    }
}
