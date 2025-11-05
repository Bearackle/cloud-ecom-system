package com.dinhhuan.repository;

import com.dinhhuan.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<Image, Long> {
}
