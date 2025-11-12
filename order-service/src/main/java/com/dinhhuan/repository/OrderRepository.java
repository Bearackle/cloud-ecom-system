package com.dinhhuan.repository;

import com.dinhhuan.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByUserId(Long userId);

//    Order findOrder(Long id);
    Optional<Order> findById(Long id);        // có sẵn từ JpaRepository, ghi rõ để dễ đọc (hoặc bỏ cũng được)
    Optional<Order> findOneById(Long id);
}
