package com.dinhhuan.service;

import com.dinhhuan.dto.request.CartItemRequest;
import com.dinhhuan.dto.response.CartItemDto;

import java.util.List;

public interface CartService {
    Long addItemToCart(CartItemRequest cartItem);
    Long removeItemFromCart(Long cartItemId);
    void increaseQuantity(Long cartItemId, Integer quantity);
    void decreaseQuantity(Long cartItemId, Integer quantity);
    List<CartItemDto> getCartItems(Long userId);
    void updateQuantity(Long cartItemId, Integer quantity);
}
