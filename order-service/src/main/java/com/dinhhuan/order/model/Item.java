package com.dinhhuan.order.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Column(name = "product_variant_id", nullable = false)
    Long productVariantId;

    @Column(nullable = false)
    @Builder.Default
    Integer quantity = 1;

    @Column(name = "price", precision = 12, scale = 2)
    BigDecimal price;

}
