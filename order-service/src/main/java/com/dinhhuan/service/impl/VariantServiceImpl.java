package com.dinhhuan.service.impl;

import com.dinhhuan.dto.request.VariantCreationRequest;
import com.dinhhuan.repository.VariantRepository;
import com.dinhhuan.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    @Override
    public Long createOrUpdate(VariantCreationRequest variantCreation) {
    }

    @Override
    public void deleteVariant(Long variantId) {

    }
}
