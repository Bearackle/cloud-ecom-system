package com.dinhhuan.order.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_variant_id"}))
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "product_variant_id", nullable = false)
    Long productVariantId;

    @Column(nullable = false)
    @Builder.Default
    Integer quantity = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_variant_id", insertable = false, updatable = false)
    ProductVariant productVariant;
}
