package com.dinhhuan.controller;

import com.dinhhuan.dto.BrandSimpleDto;
import com.dinhhuan.dto.CategoryCreation;
import com.dinhhuan.dto.CategorySimpleDto;
import com.dinhhuan.model.Category;
import com.dinhhuan.repository.CategoryRepository;
import com.dinhhuan.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategory() {
        return new ResponseEntity<>(categoryService.getTreeCategories(), HttpStatus.OK);
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CategorySimpleDto>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategorySimpleDto> getCategory(@PathVariable Long id) {
        var cat = categoryService.getCategory(id);
        if(cat == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cat);
    }
}
