package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.commons.uid.DefaultUid;
import com.dinhhuan.dto.BrandCreation;
import com.dinhhuan.model.Brand;
import com.dinhhuan.repository.BrandRepository;
import com.dinhhuan.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final DefaultUidGenerator uidGenerator;
    @Override
    public Long createBrand(BrandCreation brand) {
        Brand br = new Brand();
        br.setBrandName(brand.getBrandName());
        br.setAvtImgUrl(brand.getAvtImgUrl());
        br.setId(uidGenerator.getUID());
        brandRepository.save(br);
        return br.getId();
    }
    @Override
    public List<Brand> getBrands() {
        return brandRepository.findAll();
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
