package com.dinhhuan.service;

import com.dinhhuan.dto.BrandCreation;
import com.dinhhuan.dto.BrandSimpleDto;
import com.dinhhuan.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    Long createBrand(BrandCreation brand);
    List<BrandSimpleDto> getBrands();
    void deleteBrand(Long id);
    BrandSimpleDto getBrandById(Long id);
}
