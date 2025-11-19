package com.dinhhuan.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariantCreationRequest {
    private Long id;
    private String variantName;
    private String imgUrl;
    private Long price;
}
