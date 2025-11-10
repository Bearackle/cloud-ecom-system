package com.dinhhuan.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "order_date", nullable = false)
    LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "total_amount", nullable = false)
    Float totalAmount;

    @Column(columnDefinition = "MEDIUMTEXT")
    String note;

    @Column(nullable = false)
    Integer status = 0;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Item> items;
}
