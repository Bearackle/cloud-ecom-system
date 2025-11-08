package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.CategoryCreation;
import com.dinhhuan.dto.CategorySimpleDto;
import com.dinhhuan.model.Category;
import com.dinhhuan.repository.CategoryRepository;
import com.dinhhuan.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final DefaultUidGenerator uidGenerator;
    @Override
    public Long createCategory(CategoryCreation category) {
        Category cat = new Category();
        cat.setId(uidGenerator.getUID());
        cat.setCategoryName(category.getCategoryName());
        if(category.getParentId() != null){
            cat.setParent(Category.builder()
                    .id(category.getParentId())
                    .build());
        }
        cat.setImgUrl(category.getImgUrl());
        return categoryRepository.save(cat).getId();
    }
    @Override
    public CategorySimpleDto getCategory(Long id) {
        return categoryRepository.findById(id)
                        .map(cat -> CategorySimpleDto.builder()
                                .id(cat.getId())
                                .categoryName(cat.getCategoryName())
                                .imgUrl(cat.getImgUrl())
                                .build())
                                .orElse(null);
    }

    @Override
    public List<CategorySimpleDto> getCategories() {
        return categoryRepository.findAllCategories();
    }

    @Override
    public List<Category> getTreeCategories() {
        return categoryRepository.findByParentIsNull();
    }
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
