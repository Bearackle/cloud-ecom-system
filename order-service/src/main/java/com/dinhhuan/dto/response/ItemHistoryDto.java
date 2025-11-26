package com.dinhhuan.dto.response;

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
public class ItemHistoryDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String variantName;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long variantId;
    private Integer quantity;
    private Long price;
    private String imgUrl;
}
