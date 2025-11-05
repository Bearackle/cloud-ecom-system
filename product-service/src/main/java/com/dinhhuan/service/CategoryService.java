package com.dinhhuan.service;

import com.dinhhuan.dto.CategorySimpleDto;
import com.dinhhuan.model.Category;
import com.dinhhuan.dto.CategoryCreation;

import java.util.List;

public interface CategoryService {
    Long createCategory(CategoryCreation category);
    Category getCategory(Long id);
    List<CategorySimpleDto> getCategories();
    void deleteCategory(Long id);
    List<Category> getTreeCategories();
}
