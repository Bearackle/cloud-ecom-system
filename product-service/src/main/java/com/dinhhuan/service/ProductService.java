package com.dinhhuan.service;

import com.dinhhuan.dto.*;
import com.dinhhuan.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Long createProductDetail(ProductDetailCreation product);
    ProductDto getProductById(Long id);
    List<Product> getProducts();
    void deleteProductById(Long id);
    List<Product> filter(ProductFilterModel filter);
    List<Product> search(String keyword, ProductFilterModel filter);
    Page<ProductSimpleDto> getListProudct(Pageable pageable);
    ProductDto updateProduct(Long id, ProductDetailEdit productEdit);
}
