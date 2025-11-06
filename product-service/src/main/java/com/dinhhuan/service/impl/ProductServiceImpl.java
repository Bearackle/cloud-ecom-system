package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.*;
import com.dinhhuan.model.*;
import com.dinhhuan.repository.ProductImageRepository;
import com.dinhhuan.repository.ProductRepository;
import com.dinhhuan.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final DefaultUidGenerator uidGenerator;
    @Override
    public Long createProductDetail(ProductDetailCreation productCreation) {
        Product product = new Product();
        product.setId(uidGenerator.getUID());
        product.setProductName(productCreation.getProductName());
        product.setPrice(productCreation.getPrice());
        product.setCategory(Category.builder()
                .id(productCreation.getCategoryId()).build());
        product.setBrand(Brand.builder().id(productCreation.getBrandId()).build());
        product.setDescription(productCreation.getDescription());
        //attr
        product.setAttributes(new ArrayList<>());
        for(int i = 0 ; i < productCreation.getAttributes().size(); i++){
            ProductAttributeCreation attr = productCreation.getAttributes().get(i);
            product.getAttributes().add(Attribute.builder().id(uidGenerator.getUID()).attributeName(attr.getAttributeName())
                    .attributeValue(attr.getAttributeValue())
                            .product(product)
                    .build());
        }
        //image
        product.setImages(new ArrayList<>());
        for(int i = 0 ; i < productCreation.getImages().size(); i++) {
            ProductImage img = productCreation.getImages().get(i);
            product.getImages().add(Image.builder().id(uidGenerator.getUID()).product(product)
                    .imgUrls(img.getUrl())
                    .build());
        }
        productRepository.save(product);
        return product.getId();
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    @Override
    public void updateProduct(Product product) {
        //wait for it
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> filter(ProductFilterModel filter) {
        return List.of();
    }

    @Override
    public List<Product> search(String keyword, ProductFilterModel filter) {
        return List.of();
    }

    @Override
    public Page<ProductSimpleDto> getListProudct(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(p -> ProductSimpleDto.builder()
                        .id(p.getId())
                        .productName(p.getProductName())
                        .price(p.getPrice())
                        .imageUrl(p.getImages().getFirst().getImgUrls())
                        .build());
    }
}
