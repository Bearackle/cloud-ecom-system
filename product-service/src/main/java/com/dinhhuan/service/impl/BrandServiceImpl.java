package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.commons.uid.DefaultUid;
import com.dinhhuan.dto.BrandCreation;
import com.dinhhuan.dto.BrandSimpleDto;
import com.dinhhuan.model.Brand;
import com.dinhhuan.repository.BrandRepository;
import com.dinhhuan.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final DefaultUidGenerator uidGenerator;
    @Override
    public Long createBrand(BrandCreation brand) {
        Brand br = new Brand();
        br.setBrandName(brand.getBrandName());
        log.info(brand.getAvtImgUrl());
        br.setAvtImgUrl(brand.getAvtImgUrl());
        br.setId(uidGenerator.getUID());
        brandRepository.save(br);
        return br.getId();
    }
    @Override
    public List<BrandSimpleDto> getBrands() {
        return brandRepository.findAll()
                .stream().map(
                        b -> BrandSimpleDto.builder()
                                .id(b.getId())
                                .brandName(b.getBrandName())
                                .avtImgUrl(b.getAvtImgUrl())
                                .build()
                ).toList();
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public BrandSimpleDto getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(b -> BrandSimpleDto.builder()
                        .id(b.getId())
                        .brandName(b.getBrandName())
                        .avtImgUrl(b.getAvtImgUrl())
                        .build())
                .orElse(null);
    }

}
