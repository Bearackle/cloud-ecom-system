package com.dinhhuan.order.service;

import com.dinhhuan.order.dto.request.CartItemRequest;
import com.dinhhuan.order.dto.response.CartItemResponse;

import java.util.List;

public interface CartItemService {
    CartItemResponse addToCart(CartItemRequest request);
    List<CartItemResponse> getCartByUserId(Long userId);
    CartItemResponse updateCartItem(Long id, CartItemRequest request);
    void removeFromCart(Long id);
    void clearCart(Long userId);
}

