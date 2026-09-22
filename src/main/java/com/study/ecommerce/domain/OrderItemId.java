package com.study.ecommerce.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Composite primary key of {@link OrderItem}: the pair (order, product).
 * The same product cannot appear twice as a separate line on one order.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderItemId implements Serializable {

    private Long orderId;

    private Long productId;

    public OrderItemId(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
}
