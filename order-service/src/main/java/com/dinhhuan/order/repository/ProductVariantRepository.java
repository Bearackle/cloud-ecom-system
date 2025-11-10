package com.dinhhuan.order.repository;

import com.dinhhuan.order.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    boolean existsByName(String name);
}
