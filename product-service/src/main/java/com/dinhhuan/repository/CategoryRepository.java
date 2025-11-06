package com.dinhhuan.repository;

import com.dinhhuan.dto.CategorySimpleDto;
import com.dinhhuan.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths = {"children"})
    List<Category> findByParentIsNull();
    @Query("SELECT new com.dinhhuan.dto.CategorySimpleDto(c.id, c.categoryName, c.imgUrl) FROM Category c")
    List<CategorySimpleDto> findAllCategories();
}
