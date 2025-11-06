package com.dinhhuan.service;

import com.dinhhuan.dto.ProductDetailCreation;
import com.dinhhuan.dto.ProductFilterModel;
import com.dinhhuan.dto.ProductSimpleDto;
import com.dinhhuan.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Long createProductDetail(ProductDetailCreation product);
    Product getProductById(Long id);
    List<Product> getProducts();
    void updateProduct(Product product);
    void deleteProductById(Long id);
    List<Product> filter(ProductFilterModel filter);
    List<Product> search(String keyword, ProductFilterModel filter);
    Page<ProductSimpleDto> getListProudct(Pageable pageable);
}
