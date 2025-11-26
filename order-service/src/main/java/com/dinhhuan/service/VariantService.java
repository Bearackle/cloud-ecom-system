package com.dinhhuan.service;

import com.dinhhuan.dto.request.VariantSyncDto;

public interface VariantService {
    Long createOrUpdate(VariantSyncDto variantCreation);
}
