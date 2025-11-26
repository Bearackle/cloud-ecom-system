package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.request.VariantSyncDto;
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
    public Long createOrUpdate(VariantSyncDto variantCreation) {
        var entity = variantRepository.findById(variantCreation.getId()).orElse(
                new ProductVariant()
        );
        if(entity.getId() == null){
            entity.setId(variantCreation.getId());
        }
        entity.setName(variantCreation.getVariantName());
        entity.setImgUrl(variantCreation.getImgUrl());
        entity.setPrice(variantCreation.getPrice());
        variantRepository.save(entity);
        return entity.getId();
    }
}
