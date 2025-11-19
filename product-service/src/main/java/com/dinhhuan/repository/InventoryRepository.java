package com.dinhhuan.repository;

import com.dinhhuan.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByOrderId(Long orderId);
}
