package com.dinhhuan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandSimpleDto {
    private String id;
    private String brandName;
    private String avtImgUrl;
}
