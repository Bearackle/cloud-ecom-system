package com.dinhhuan.repository;

import com.dinhhuan.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByUserId(Long userId);

    Order findOrder(Long id);
}
