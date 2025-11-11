package com.dinhhuan.common;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Data<T>{
    private long code;
    private T data;
    private String message;
}

