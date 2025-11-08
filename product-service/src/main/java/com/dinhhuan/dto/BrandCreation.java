package com.dinhhuan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
public class BrandCreation {
    private String brandName;
    private String avtImgUrl;
}
