package com.dinhhuan.controller;

import com.dinhhuan.common.Data;
import com.dinhhuan.dto.request.ProductVariantRequest;
import com.dinhhuan.dto.response.ProductVariantResponse;
import com.dinhhuan.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<Data<Map<String, Long>>> create(@RequestBody ProductVariantRequest request) {
        Long id = productVariantService.createProductVariant(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Data.<Map<String, Long>>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Product variant created successfully")
                        .data(Map.of("id", id))
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data<ProductVariantResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(Data.<ProductVariantResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(productVariantService.getProductVariantById(id))
                .build());
    }

    @GetMapping
    public ResponseEntity<Data<List<ProductVariantResponse>>> getAll() {
        return ResponseEntity.ok(Data.<List<ProductVariantResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(productVariantService.getAllProductVariants())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data<ProductVariantResponse>> update(@PathVariable Long id, @RequestBody ProductVariantRequest request) {
        return ResponseEntity.ok(Data.<ProductVariantResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Product variant updated successfully")
                .data(productVariantService.updateProductVariant(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data<Map<String, String>>> delete(@PathVariable Long id) {
        productVariantService.deleteProductVariant(id);
        return ResponseEntity.ok(Data.<Map<String, String>>builder()
                .code(HttpStatus.OK.value())
                .message("Product variant deleted successfully")
                .data(Map.of("id", String.valueOf(id)))
                .build());
    }
}
