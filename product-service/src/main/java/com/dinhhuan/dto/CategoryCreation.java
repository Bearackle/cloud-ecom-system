package com.dinhhuan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreation {
    private String categoryName;
    private Long parentId;
    private String imgUrl;
}
