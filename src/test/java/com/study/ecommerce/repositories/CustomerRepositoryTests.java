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

import com.study.ecommerce.domain.Customer;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Order(1)
    void shouldFindCustomerById() {
        Customer savedCustomer = customerRepository.saveAndFlush(new Customer(
            "Customer to find",
            "find.customer@example.com",
            "+55 11 91111-1111"
        ));

        Customer foundCustomer = customerRepository
            .findById(savedCustomer.getId())
            .orElseThrow();

        assertEquals(savedCustomer.getId(), foundCustomer.getId());
        assertEquals("Customer to find", foundCustomer.getName());
        assertEquals("find.customer@example.com", foundCustomer.getEmail());
    }

    @Test
    @Order(2)
    void shouldListAllCustomers() {
        long quantityBeforeInsert = customerRepository.count();
        customerRepository.saveAndFlush(new Customer(
            "First listed customer",
            "first.listed.customer@example.com",
            "+55 11 91234-0001"
        ));
        customerRepository.saveAndFlush(new Customer(
            "Second listed customer",
            "second.listed.customer@example.com",
            "+55 11 91234-0002"
        ));

        List<Customer> customers = customerRepository.findAll();

        assertEquals(quantityBeforeInsert + 2, customers.size());
        assertTrue(customers.stream()
            .anyMatch(customer -> customer.getName().equals("First listed customer")));
        assertTrue(customers.stream()
            .anyMatch(customer -> customer.getName().equals("Second listed customer")));
    }

    @Test
    @Order(3)
    void shouldInsertCustomer() {
        Customer customer = new Customer(
            "New Customer",
            "new.customer@example.com",
            "+55 11 99999-9999"
        );

        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        assertNotNull(savedCustomer.getId());
        assertTrue(customerRepository.existsById(savedCustomer.getId()));
        assertEquals("New Customer", savedCustomer.getName());
    }

    @Test
    @Order(4)
    void shouldUpdateCustomer() {
        Customer customer = new Customer(
            "Old Name",
            "update.customer@example.com",
            "+55 11 98888-8888"
        );
        customerRepository.save(customer);
        Long customerId = customer.getId();

        customer.setName("Updated Name");
        customerRepository.saveAndFlush(customer);

        Customer updatedCustomer = customerRepository
            .findById(customerId)
            .orElseThrow();

        assertEquals(customerId, updatedCustomer.getId());
        assertEquals("Updated Name", updatedCustomer.getName());
    }

    @Test
    @Order(5)
    void shouldDeleteCustomerById() {
        Customer customer = customerRepository.save(new Customer(
            "Customer to delete",
            "delete.customer@example.com",
            "+55 11 97777-7777"
        ));

        Long customerId = customer.getId();
        assertTrue(customerRepository.existsById(customerId));

        customerRepository.deleteById(customerId);

        assertFalse(customerRepository.existsById(customerId));
    }
}
