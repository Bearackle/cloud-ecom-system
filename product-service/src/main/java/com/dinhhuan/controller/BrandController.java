package com.dinhhuan.controller;

import com.dinhhuan.dto.BrandCreation;
import com.dinhhuan.dto.BrandSimpleDto;
import com.dinhhuan.dto.CategoryCreation;
import com.dinhhuan.model.Brand;
import com.dinhhuan.model.Category;
import com.dinhhuan.service.BrandService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<Map<String, Long>> create(@RequestBody BrandCreation brand) {
        Long id = brandService.createBrand(brand);
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> getBrands() {
        return new ResponseEntity<>(brandService.getBrands(), HttpStatus.OK);
    }
}
