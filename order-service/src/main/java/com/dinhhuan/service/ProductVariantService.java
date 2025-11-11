package com.dinhhuan.service;

import com.dinhhuan.dto.request.ProductVariantRequest;
import com.dinhhuan.dto.response.ProductVariantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductVariantService {
    Long createProductVariant(ProductVariantRequest productVariantRequest);
    ProductVariantResponse getProductVariantById(Long id);
    List<ProductVariantResponse> getAllProductVariants();
    Page<ProductVariantResponse> getAllProductVariants(Pageable pageable);
    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest);
    void deleteProductVariant(Long id);
}

