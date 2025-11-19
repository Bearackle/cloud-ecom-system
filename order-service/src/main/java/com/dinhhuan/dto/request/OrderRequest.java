package com.dinhhuan.dto.request;

import com.dinhhuan.dto.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long addressId;
    private Long totalAmount;
    private String note;
    private Integer status;
    private List<ItemRequest> items;
    private PaymentMethod method;
}

