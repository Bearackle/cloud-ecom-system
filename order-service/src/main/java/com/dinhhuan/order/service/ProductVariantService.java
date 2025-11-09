package com.dinhhuan.order.service;

import com.dinhhuan.order.dto.request.ProductVariantRequest;
import com.dinhhuan.order.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {
    ProductVariantResponse createProductVariant(ProductVariantRequest request);
    List<ProductVariantResponse> getAll();
    ProductVariantResponse getById(Long id);
    ProductVariantResponse update(Long id, ProductVariantRequest request);
    void delete(Long id);
}

