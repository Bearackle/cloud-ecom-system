package com.dinhhuan.repository;

import com.dinhhuan.model.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findAllByUserId(Long userId);
    @NonNull
    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findById(@NonNull Long id);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user u LEFT JOIN FETCH o.items i where o.id = :id")
    Optional<Order> findOrderDetailsIncluded(Long id);
    @EntityGraph(attributePaths = {"items", "address"})
    List<Order> findAllByUser_Id(Long userId);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user u LEFT JOIN FETCH o.address a where o.id = :id")
    Order findByOrderIdIncludeUserAddress(Long id);
}
