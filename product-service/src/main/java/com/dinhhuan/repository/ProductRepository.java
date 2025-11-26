package com.dinhhuan.repository;

import com.dinhhuan.model.Category;
import com.dinhhuan.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop5ByOrderByPriceAsc();
    Page<Product> findAllByCategory_Id(Long categoryId, Pageable pageable);
}
