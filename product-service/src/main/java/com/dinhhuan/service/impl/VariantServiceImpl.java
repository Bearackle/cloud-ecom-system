package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.VariantCreation;
import com.dinhhuan.dto.VariantDto;
import com.dinhhuan.model.Product;
import com.dinhhuan.model.ProductVariant;
import com.dinhhuan.producer.VariantCreationEvent;
import com.dinhhuan.repository.ProductVariantRepository;
import com.dinhhuan.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
    private final ProductVariantRepository productVariantRepository;
    private final DefaultUidGenerator uid;
    private final VariantCreationEvent event;
    @Override
    public Long createVariant(VariantCreation variantCreation) {
        ProductVariant variant = new ProductVariant();
        variant.setId(uid.getUID());
        variant.setProductVariantName(variantCreation.getVariantName());
        variant.setQuantity(variantCreation.getQuantity());
        variant.setImgUrl(variantCreation.getImgUrl());
        variant.setProduct(Product.builder().id(variantCreation.getProductId()).build());
        productVariantRepository.save(variant);
        //send event create variant
        event.sendMessage(VariantDto.builder()
                .id(variant.getId())
                .variantName(variant.getProductVariantName())
                .imgUrl(variant.getImgUrl())
                .build());
        return variant.getId();
    }
    @Override
    public List<VariantDto> getVariantOfProductId(Long productId) {
        List<ProductVariant> variants = productVariantRepository.findByProductId(productId);
        return variants.stream().map(v -> VariantDto.builder()
                .id(v.getId())
                .variantName(v.getProductVariantName())
                .quantity(v.getQuantity())
                .imgUrl(v.getImgUrl())
                        .productId(v.getProduct().getId())
                        .build())
                .toList();
    }
    @Override
    public VariantDto updateVariant(Long variantId, VariantDto variantDto) {
        ProductVariant pv = productVariantRepository.findById(variantId).orElse(null);
        if(pv == null) {
            return null;
        }
        pv.setProductVariantName(variantDto.getVariantName());
        pv.setQuantity(variantDto.getQuantity());
        pv.setImgUrl(variantDto.getImgUrl());
        pv.setProduct(Product.builder().id(variantDto.getProductId()).build());
        productVariantRepository.save(pv);
        return findVariantById(variantId);
    }

    @Override
    public void deleteVariant(Long variantId) {
        productVariantRepository.deleteById(variantId);
    }
    @Override
    public VariantDto findVariantById(Long variantId) {
        var pv=  productVariantRepository.findById(variantId).orElse(null);
        if(pv == null) {
            return null;
        }
        return VariantDto.builder()
                .id(pv.getId())
                .variantName(pv.getProductVariantName())
                .quantity(pv.getQuantity())
                .imgUrl(pv.getImgUrl())
                .productId(pv.getProduct().getId())
                .build();
    }

    @Override
    public Page<VariantDto> getVariantList(Pageable pageable) {
        return productVariantRepository.findAll(pageable)
                .map(p -> VariantDto.builder()
                        .id(p.getId())
                        .variantName(p.getProductVariantName())
                        .quantity(p.getQuantity())
                        .productId(p.getProduct().getId())
                        .imgUrl(p.getImgUrl()).build());
    }
}
