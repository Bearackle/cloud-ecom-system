package com.dinhhuan.service;

import com.dinhhuan.dto.response.CartItemDto;

import java.util.List;

public interface CartService {
    Long addItemToCart(Long variantId);
    Long removeItemFromCart(Long variantId);
    void increaseQuantity(Long variantId, Integer quantity);
    void decreaseQuantity(Long variantId, Integer quantity);
    List<CartItemDto> getCartItems(Long userId);
}
