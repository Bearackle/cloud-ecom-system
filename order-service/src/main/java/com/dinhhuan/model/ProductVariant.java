package com.dinhhuan.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    Long id;
    @Column(name = "variant_name", nullable = false, length = 200)
    String name;
    @Column(name = "img_url", length = 400)
    String imgUrl;
}