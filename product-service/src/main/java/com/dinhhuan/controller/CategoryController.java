package com.dinhhuan.controller;

import com.dinhhuan.dto.CategoryCreation;
import com.dinhhuan.dto.CategorySimpleDto;
import com.dinhhuan.model.Category;
import com.dinhhuan.repository.CategoryRepository;
import com.dinhhuan.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<Map<String, Long>> create(@RequestBody CategoryCreation category) {
        Long id = categoryService.createCategory(category);
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CategorySimpleDto>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategory() {
        return new ResponseEntity<>(categoryService.getTreeCategories(), HttpStatus.OK);
    }
}
