package com.study.ecommerce.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A product category (e.g. "Books", "Electronics").
 * A category groups many products, and a product may belong to many categories,
 * so this is the inverse side of a many-to-many association owned by {@link Product}.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new LinkedHashSet<>();

    public Category(String name) {
        this.name = name;
    }
}
