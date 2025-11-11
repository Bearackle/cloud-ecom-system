package com.dinhhuan.service;

import com.dinhhuan.dto.request.VariantCreationRequest;

public interface VariantService {
    Long createOrUpdate(VariantCreationRequest variantCreation);
}
