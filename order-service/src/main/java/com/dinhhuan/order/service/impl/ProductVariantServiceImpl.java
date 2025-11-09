package com.dinhhuan.order.service.impl;

import com.dinhhuan.order.common.AppEx;
import com.dinhhuan.order.dto.request.ProductVariantRequest;
import com.dinhhuan.order.dto.response.ProductVariantResponse;
import com.dinhhuan.order.mapper.ProductVariantMapper;
import com.dinhhuan.order.model.ProductVariant;
import com.dinhhuan.order.repository.ProductVariantRepository;
import com.dinhhuan.order.service.ProductVariantService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    public ProductVariantResponse createProductVariant(ProductVariantRequest request) {
        ProductVariant productVariant = productVariantMapper.toEntity(request);
        LocalDateTime now = LocalDateTime.now();
        productVariant.setCreatedAt(now);
        ProductVariant saved = productVariantRepository.save(productVariant);
        return productVariantMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public List<ProductVariantResponse> getAll() {
        return productVariantRepository.findAll().stream()
                .map(productVariantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductVariantResponse getById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new AppEx("Product variant not found with id: " + id, HttpStatus.NOT_FOUND));
        return productVariantMapper.toResponse(productVariant);
    }

    @Override
    @Transactional
    public ProductVariantResponse update(Long id, ProductVariantRequest request) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new AppEx("Product variant not found with id: " + id, HttpStatus.NOT_FOUND));

        productVariant.setName(request.getName());
        productVariant.setImgUrl(request.getImgUrl());
        productVariant.setUpdatedAt(LocalDateTime.now());

        ProductVariant updated = productVariantRepository.save(productVariant);
        return productVariantMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productVariantRepository.existsById(id)) {
            throw new AppEx("Product variant not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        productVariantRepository.deleteById(id);
    }
}
