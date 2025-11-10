package com.dinhhuan.model;

import com.dinhhuan.enums.OrderStatus;
import com.dinhhuan.model.Item;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "order_date", nullable = false)
    @Builder.Default
    LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "total_amount", nullable = false)
    Long totalAmount;

    @Column(name = "note", columnDefinition = "TEXT")
    String note;

    @Column(name = "status",nullable = false)
    @Builder.Default
    Integer status = 0;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Item> items;
}