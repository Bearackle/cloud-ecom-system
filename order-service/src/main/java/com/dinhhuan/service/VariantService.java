package com.dinhhuan.service;

import com.dinhhuan.dto.request.VariantCreationRequest;

public interface VariantService {
    Long createVariant(VariantCreationRequest variantCreation);
    void deleteVariant(Long variantId);
}
