package com.dinhhuan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public interface CategorySimpleDto {
     Long id();
     String categoryName();
     String imgUrl();
}
