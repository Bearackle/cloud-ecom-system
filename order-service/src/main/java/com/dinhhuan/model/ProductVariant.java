package com.dinhhuan.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    Long id;
    @Column(nullable = false, length = 200)
    String name;
    @Column(name = "img_url", length = 500)
    String imgUrl;
}