package com.dinhhuan.controller;

import com.dinhhuan.dto.*;
import com.dinhhuan.model.Product;
import com.dinhhuan.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<ProductSimpleDto>> getProducts(
            @RequestParam(name = "_page", defaultValue = "1") int page,
            @RequestParam(name = "_perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "_sort", defaultValue = "id") String sort,
            @RequestParam(name = "_order", defaultValue = "ASC") String order
    )
    {
        Pageable pageable = PageRequest.of(
                page - 1,
                perPage,
                order.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort
        );
        Page<ProductSimpleDto> productList = productService.getListProudct(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", String.format("brands %d-%d/%d",
                (page - 1) * perPage,
                Math.min(page * perPage - 1, (int) productList.getTotalElements() - 1),
                productList.getTotalElements()
        ));
        PageResponse<ProductSimpleDto> response = new PageResponse<>(
                productList.getContent(),
                MetaPageResponse.builder()
                        .page(page)
                        .perPage(perPage)
                        .totalElements(productList.getTotalElements())
                        .totalPages(productList.getTotalPages())
                        .build()
        );
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }
    @RequestMapping(value = "", method =  RequestMethod.POST)
    public ResponseEntity<Map<String, Long>> createProduct(@RequestBody ProductDetailCreation productCreation) {
        Long id = productService.createProductDetail(productCreation);
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public ResponseEntity<ProductDto> get (@PathVariable Long id) {
        var product = productService.getProductById(id);
        if(product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(product);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDetailEdit productDetailEdit) {
         var product = productService.updateProduct(id, productDetailEdit);
         if(product == null) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         return ResponseEntity.ok(product);
    }
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok(Map.of("id", String.valueOf(id)));
    }
    @RequestMapping(value="/ref", method = RequestMethod.GET)
    public ResponseEntity<List<ProductRef>> getProductRef() {
        var productRef = productService.getProductRefs();
        if(productRef == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(productRef);
    }
    @RequestMapping(value = "top-deals", method = RequestMethod.GET)
    public ResponseEntity<List<ProductSimpleDto>> getProductTopDeal(){
        var products = productService.getProductTopDeal();
        if(products == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(products);
    }
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<ProductSimpleDto>> getProducts(
            @PathVariable String id,
            @RequestParam(name = "_page", defaultValue = "1") int page,
            @RequestParam(name = "_perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "_sort", defaultValue = "id") String sort,
            @RequestParam(name = "_order", defaultValue = "ASC") String order
    )
    {
        Pageable pageable = PageRequest.of(
                page - 1,
                perPage,
                order.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort
        );
        Page<ProductSimpleDto> productList = productService.getProductByCategory(pageable,Long.valueOf(id));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", String.format("brands %d-%d/%d",
                (page - 1) * perPage,
                Math.min(page * perPage - 1, (int) productList.getTotalElements() - 1),
                productList.getTotalElements()
        ));
        PageResponse<ProductSimpleDto> response = new PageResponse<>(
                productList.getContent(),
                MetaPageResponse.builder()
                        .page(page)
                        .perPage(perPage)
                        .totalElements(productList.getTotalElements())
                        .totalPages(productList.getTotalPages())
                        .build()
        );
        System.out.println(productList.stream().count());
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }
}
