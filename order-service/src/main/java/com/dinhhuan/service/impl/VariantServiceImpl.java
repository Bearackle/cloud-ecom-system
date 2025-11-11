package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.request.VariantCreationRequest;
import com.dinhhuan.model.ProductVariant;
import com.dinhhuan.repository.VariantRepository;
import com.dinhhuan.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
    private final VariantRepository variantRepository;
    private final DefaultUidGenerator uid;
    @Override
    public Long createOrUpdate(VariantCreationRequest variantCreation) {
        var entity = variantRepository.findById(variantCreation.getId()).orElse(
                new ProductVariant()
        );
        if(entity.getId() == null){
            entity.setId(uid.getUID());
        }
        entity.setName(variantCreation.getVariantName());
        entity.setImgUrl(variantCreation.getImgUrl());
        variantRepository.save(entity);
        return entity.getId();
    }
}
