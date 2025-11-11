package com.dinhhuan.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String variantName;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;
    private Integer quantity;
    private String imgUrl;
}
