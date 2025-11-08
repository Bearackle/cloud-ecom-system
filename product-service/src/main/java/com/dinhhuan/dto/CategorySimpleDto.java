package com.dinhhuan.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySimpleDto {
     @JsonSerialize(using = ToStringSerializer.class)
     private Long id;
     private String categoryName;
     private String imgUrl;
}
