package com.dinhhuan.order.controller;

import com.dinhhuan.commons.regular.Data;
import com.dinhhuan.order.dto.request.ProductVariantRequest;
import com.dinhhuan.order.dto.response.ProductVariantResponse;
import com.dinhhuan.order.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-variants")
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<Data<ProductVariantResponse>> createProductVariant(@RequestBody ProductVariantRequest request) {
        ProductVariantResponse response = productVariantService.createProductVariant(request);
        Data<ProductVariantResponse> data = Data.<ProductVariantResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Product variant created successfully")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @GetMapping
    public ResponseEntity<Data<List<ProductVariantResponse>>> getAllProductVariants() {
        List<ProductVariantResponse> variants = productVariantService.getAll();
        Data<List<ProductVariantResponse>> data = Data.<List<ProductVariantResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(variants)
                .build();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data<ProductVariantResponse>> getProductVariantById(@PathVariable Long id) {
        ProductVariantResponse response = productVariantService.getById(id);
        Data<ProductVariantResponse> data = Data.<ProductVariantResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(response)
                .build();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data<ProductVariantResponse>> updateProductVariant(@PathVariable Long id, @RequestBody ProductVariantRequest request) {
        ProductVariantResponse response = productVariantService.update(id, request);
        Data<ProductVariantResponse> data = Data.<ProductVariantResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Product variant updated successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Long id) {
        productVariantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

