package com.dinhhuan.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private Long id;
    @Column(name = "brand_name", length = 100)
    private String brandName;

    @Column(name = "avt_img_url", length = 300)
    private String avtImgUrl;
}