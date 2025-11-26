package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.*;
import com.dinhhuan.model.*;
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
    public ProductDto getProductById(Long id) {
        var p =  productRepository.findById(id)
                .orElse(null);
        if(p == null){
            return null;
        }
        return ProductDto.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .price(p.getPrice())
                .brandId(p.getBrand().getId())
                .categoryId(p.getCategory().getId())
                .description(p.getDescription())
                .attributes(p.getAttributes()
                                .stream().map(attr -> AttributeDto.builder()
                                .id(attr.getId())
                                .attributeName(attr.getAttributeName())
                                .attributeValue(attr.getAttributeValue())
                                .build())
                        .toList())
                .images(p.getImages().stream().map(
                        img -> ImageDto.builder()
                                .id(img.getId())
                                .imgUrl(img.getImgUrls())
                                .build())
                        .toList())
                .build();
    }
    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
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

    @Override
    public ProductDto updateProduct(Long id,ProductDetailEdit productEdit) {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return null;
        }
        product.setProductName(productEdit.getProductName());
        product.setPrice(productEdit.getPrice());
        product.setCategory(Category.builder()
                .id(productEdit.getCategoryId()).build());
        product.setBrand(Brand.builder().id(productEdit.getBrandId()).build());
        product.setDescription(productEdit.getDescription());

        if(productEdit.getAttributes() != null) {
            product.getAttributes().clear();
            product.getAttributes().addAll(
                    productEdit.getAttributes()
                            .stream().map(
                                    attr -> Attribute.builder()
                                            .id(attr.getId())
                                            .product(product)
                                            .attributeName(attr.getAttributeName())
                                            .attributeValue(attr.getAttributeValue())
                                            .build()
                            ).toList()
            );
        }
        if(productEdit.getImages() != null) {
            product.getImages().clear();
            product.getImages().addAll(
                    productEdit.getImages()
                            .stream()
                            .map(img -> Image.builder()
                                    .id(img.getId())
                                    .imgUrls(img.getImgUrl())
                                    .product(product)
                                    .build()
                            ).toList());
        }
        productRepository.save(product);
        return getProductById(id);
    }
    public List<ProductRef> getProductRefs() {
        return productRepository.findAll()
                .stream()
                .map(p -> ProductRef.builder()
                        .id(p.getId())
                        .productName(p.getProductName())
                        .build())
                .toList();
    }
    @Override
    public List<ProductSimpleDto> getProductTopDeal() {
        return productRepository.findTop5ByOrderByPriceAsc()
                .stream().map( p ->
                        ProductSimpleDto.builder()
                                .id(p.getId())
                                .productName(p.getProductName())
                                .price(p.getPrice())
                                .imageUrl(p.getImages().getFirst().getImgUrls())
                                .build()
                ).toList();
    }

    @Override
    public Page<ProductSimpleDto> getProductByCategory(Pageable page, Long category) {
        return productRepository.findAllByCategory_Id(category
                , page)
                .map(
                        p -> ProductSimpleDto.builder()
                                .id(p.getId())
                                .productName(p.getProductName())
                                .price(p.getPrice())
                                .imageUrl(p.getImages().getFirst().getImgUrls())
                                .build()
                );
    }
}
