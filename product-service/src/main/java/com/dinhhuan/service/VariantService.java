package com.dinhhuan.service;

import com.dinhhuan.dto.VariantCreation;
import com.dinhhuan.dto.VariantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VariantService {
    Long createVariant(VariantCreation variantCreation);
    List<VariantDto> getVariantOfProductId(Long productId);
    VariantDto updateVariant(Long variantId, VariantDto variantDto);
    void deleteVariant(Long variantId);
    VariantDto findVariantById(Long variantId);
    Page<VariantDto> getVariantList(Pageable pageable);
}
