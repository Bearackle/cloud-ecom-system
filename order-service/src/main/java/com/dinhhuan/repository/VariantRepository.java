package com.dinhhuan.repository;

import com.dinhhuan.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<ProductVariant, Long> {
}
