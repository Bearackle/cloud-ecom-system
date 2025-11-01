package com.dinhhuan.commons.regular;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Data<T>{
    private long code;
    private T data;
    private String message;
}
