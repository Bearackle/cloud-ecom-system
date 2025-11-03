package com.dinhhuan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {

    @Id
    private Long id;

    @Column(name = "brand_name", length = 100)
    private String brandName;

    @Column(name = "avt_img_url", length = 300)
    private String avtImgUrl;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Product> products;
}