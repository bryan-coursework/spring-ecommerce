package com.study.ecommerce.domain.enums;

/**
 * Lifecycle of an {@link com.study.ecommerce.domain.Order}.
 * Stored in the database as a string (see the {@code @Enumerated} mapping on the entity).
 */
public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELED
}
