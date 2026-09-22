package com.study.ecommerce.domain;

import com.study.ecommerce.domain.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The settlement of an {@link Order}. Modelled as a one-to-one that shares the order's
 * primary key ({@code @MapsId}): a payment cannot exist without its order and there is
 * at most one per order.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Instant paidAt = Instant.now();

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    public Payment(Order order, PaymentMethod method, BigDecimal amount) {
        this.order = order;
        this.method = method;
        this.amount = amount;
    }
}
