package com.dinhhuan.repository;

import com.dinhhuan.model.ImageBlob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageBlob, Long> {
}
