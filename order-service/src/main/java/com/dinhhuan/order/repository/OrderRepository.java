package com.dinhhuan.order.repository;

import com.dinhhuan.order.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = {"items"})
    List<Order> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"items"})
    @Override
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"items"})
    @Override
    List<Order> findAll();
}
