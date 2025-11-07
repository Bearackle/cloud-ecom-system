package com.dinhhuan.dto;

import com.dinhhuan.model.Attribute;
import com.dinhhuan.model.Image;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String productName;
    private Long price;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long brandId;
    private String description;
    private List<AttributeDto> attributes;
    private List<ImageDto> images;
}
