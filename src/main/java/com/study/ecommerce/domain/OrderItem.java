package com.study.ecommerce.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Association entity that ties a {@link Product} to an {@link Order} (the "join" the
 * assignment asks for). It is a many-to-one on each side and also carries the line's
 * own data: how many units and the unit price captured at purchase time, so later
 * price changes on the product never rewrite past orders.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderItem {

    @EmbeddedId
    private OrderItemId id = new OrderItemId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    public OrderItem(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    /** Line total: unit price times quantity. */
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
