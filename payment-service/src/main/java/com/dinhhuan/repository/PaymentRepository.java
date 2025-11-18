package com.dinhhuan.repository;

import com.dinhhuan.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN p.order o WHERE o.userId = :userId")
    List<Payment> findAllByUserId(Long userId);
    List<Payment> findByOrderIdOrderByCreatedDateDesc(Long orderId);
}
