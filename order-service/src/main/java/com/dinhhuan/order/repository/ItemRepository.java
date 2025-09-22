package com.dinhhuan.order.repository;

import com.dinhhuan.order.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
