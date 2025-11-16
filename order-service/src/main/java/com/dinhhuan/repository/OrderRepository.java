package com.dinhhuan.repository;

import com.dinhhuan.model.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findAllByUserId(Long userId);
    @NonNull
    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findById(@NonNull Long id);
}
