package com.dinhhuan.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_imgs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "img_url", length = 300)
    private String imgUrls;
}
