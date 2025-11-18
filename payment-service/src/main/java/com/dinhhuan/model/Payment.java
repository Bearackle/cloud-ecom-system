package com.dinhhuan.model;

import com.dinhhuan.dto.enums.PaymentMethod;
import com.dinhhuan.dto.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus status;
    @Column(name = "method")
    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod method;
}
