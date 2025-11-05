package com.dinhhuan.service;

import com.dinhhuan.dto.BrandCreation;
import com.dinhhuan.model.Brand;

import java.util.List;

public interface BrandService {
    Long createBrand(BrandCreation brand);
    List<Brand> getBrands();
    void deleteBrand(Long id);
}
