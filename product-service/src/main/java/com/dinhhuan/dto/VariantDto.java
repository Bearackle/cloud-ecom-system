package com.dinhhuan.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariantDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String variantName;
    private Integer quantity;
    private String imgUrl;
}
