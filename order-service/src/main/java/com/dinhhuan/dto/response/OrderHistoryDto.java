package com.dinhhuan.dto.response;

import com.dinhhuan.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String addressDetail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    private Long totalAmount;
    private String note;
    private OrderStatus status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ItemHistoryDto> products;
}
