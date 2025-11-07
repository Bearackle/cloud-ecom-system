package com.dinhhuan.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String imgUrls;
}
