package com.dinhhuan.controller;

import com.dinhhuan.dto.BrandCreation;
import com.dinhhuan.dto.BrandSimpleDto;
import com.dinhhuan.dto.CategoryCreation;
import com.dinhhuan.dto.ProductSimpleDto;
import com.dinhhuan.model.Brand;
import com.dinhhuan.model.Category;
import com.dinhhuan.service.BrandService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<BrandSimpleDto>> getBrands() {
        return new ResponseEntity<>(brandService.getBrands(), HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BrandSimpleDto> getBrand(@PathVariable Long id) {
        var br = brandService.getBrandById(id);
        if(br == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(br);
    }
}
