package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.request.ProductVariantRequest;
import com.dinhhuan.dto.response.ProductVariantResponse;
import com.dinhhuan.model.ProductVariant;
import com.dinhhuan.repository.VariantRepository;
import com.dinhhuan.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final VariantRepository variantRepository;
    private final DefaultUidGenerator uidGenerator;

    @Override
    @Transactional
    public Long createProductVariant(ProductVariantRequest request) {
        ProductVariant variant = ProductVariant.builder()
                .id(uidGenerator.getUID())
                .name(request.getName())
                .imgUrl(request.getImgUrl())
                .build();
        return variantRepository.save(variant).getId();
    }

    @Override
    public ProductVariantResponse getProductVariantById(Long id) {
        return toDto(findVariant(id));
    }

    @Override
    public List<ProductVariantResponse> getAllProductVariants() {
        return variantRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Page<ProductVariantResponse> getAllProductVariants(Pageable pageable) {
        return variantRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional
    public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest request) {
        ProductVariant variant = findVariant(id);
        if (request.getName() != null) variant.setName(request.getName());
        if (request.getImgUrl() != null) variant.setImgUrl(request.getImgUrl());
        return toDto(variantRepository.save(variant));
    }

    @Override
    @Transactional
    public void deleteProductVariant(Long id) {
        variantRepository.deleteById(id);
    }

    private ProductVariant findVariant(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new AppEx("ProductVariant not found: " + id, HttpStatus.NOT_FOUND));
    }

    private ProductVariantResponse toDto(ProductVariant variant) {
        return ProductVariantResponse.builder()
                .id(variant.getId())
                .name(variant.getName())
                .imgUrl(variant.getImgUrl())
                .build();
    }
}
