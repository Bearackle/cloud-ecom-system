package com.dinhhuan.dto.request;

import com.dinhhuan.enums.OrderStatus;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    private OrderStatus status;
}
