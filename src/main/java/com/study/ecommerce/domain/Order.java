package com.study.ecommerce.domain;

import com.study.ecommerce.domain.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A purchase placed by a {@link Customer}. Table is named {@code orders} because
 * {@code ORDER} is a reserved SQL keyword.
 *
 * <p>An order aggregates its {@link OrderItem}s and has exactly one {@link Payment}.</p>
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant placedAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new LinkedHashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    public Order(Customer customer) {
        this.customer = customer;
    }

    /** Convenience helper that keeps both sides of the association in sync. */
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

    /** Sum of every line's subtotal. Not persisted; derived on demand. */
    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
