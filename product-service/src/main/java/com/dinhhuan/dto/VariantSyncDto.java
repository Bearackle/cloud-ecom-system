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
public class VariantSyncDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String variantName;
    private Long price;
    private String imgUrl;
}
