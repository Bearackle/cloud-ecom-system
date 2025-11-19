package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.CreateOrderDto;
import com.dinhhuan.model.Inventory;
import com.dinhhuan.model.Product;
import com.dinhhuan.model.ProductVariant;
import com.dinhhuan.repository.InventoryRepository;
import com.dinhhuan.repository.ProductVariantRepository;
import com.dinhhuan.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final DefaultUidGenerator uid;
    @Override
    @Transactional
    public void proceedOrder(CreateOrderDto createOrderDto) {
        List<Inventory> ls = createOrderDto.getItems().stream()
                .map(i -> Inventory.builder()
                        .id(uid.getUID())
                        .orderId(createOrderDto.getOrderId())
                        .variant(ProductVariant.builder()
                                .id(i.getVariantId())
                                .build())
                        .quantity(i.getQuantity()).build()
                ).toList();
        List<ProductVariant> variants = createOrderDto.getItems()
                        .stream().map(
                                i -> {
                                    var variant = productVariantRepository.findById(i.getVariantId()).orElse(null);
                                    if (variant == null) {
                                        throw new RuntimeException("variant not found");
                                    }
                                    if(variant.getQuantity() < i.getQuantity()){
                                        throw new RuntimeException("variant not enough");
                                    }
                                    variant.setQuantity(variant.getQuantity() - i.getQuantity());
                                    return variant;
                                }
                )
                        .toList();
        productVariantRepository.saveAll(variants);
        inventoryRepository.saveAll(ls);
    }

    @Override
    public void rollbackOrder(Long orderId) {
        List<ProductVariant> variants = new ArrayList<>();
        var ls = inventoryRepository.findAllByOrderId(orderId);
        if(ls.isEmpty()){
            return;
        }
        ls.forEach(
                i -> {
                    var variant = productVariantRepository.findById(i.getVariant().getId()).orElse(null);
                    if (variant != null) {
                        variant.setQuantity(variant.getQuantity() + i.getQuantity());
                    }
                    variants.add(variant);
                }
        );
        productVariantRepository.saveAll(variants);
    }
}
