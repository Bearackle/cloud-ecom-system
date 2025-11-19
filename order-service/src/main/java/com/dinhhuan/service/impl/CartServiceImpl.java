package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.request.CartItemRequest;
import com.dinhhuan.dto.response.CartItemDto;
import com.dinhhuan.model.CartItem;
import com.dinhhuan.model.ProductVariant;
import com.dinhhuan.model.User;
import com.dinhhuan.repository.CartItemRepository;
import com.dinhhuan.repository.UserRepository;
import com.dinhhuan.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final DefaultUidGenerator uidGenerator;

    @Override
    public Long addItemToCart(CartItemRequest cartItem) {
        var item = cartItemRepository.findByUserIdAndVariantId(cartItem.getUserId(), cartItem.getVariantId())
                .orElse(CartItem.builder().id(uidGenerator.getUID())
                        .build());
            item.setUser(User.builder().id(cartItem.getUserId()).build());
            item.setVariant(ProductVariant.builder().id(cartItem.getVariantId()).build());
            if (cartItem.getQuantity() == null) {
                if(item.getQuantity() == null) {
                    cartItem.setQuantity(1);
                } else
                    cartItem.setQuantity(item.getQuantity()+1);
            }
            item.setQuantity(cartItem.getQuantity());
            cartItemRepository.save(item);
            return item.getId();
    }
    @Override
    @Transactional
    public Long removeItemFromCart(Long cartItemId) {
        CartItem cartItem = findCartItem(cartItemId);
        cartItemRepository.delete(cartItem);
        return cartItem.getId();
    }

    @Override
    @Transactional
    public void increaseQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = findCartItem(cartItemId);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void decreaseQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = findCartItem(cartItemId);
        int newQuantity = cartItem.getQuantity() - quantity;
        newQuantity = Math.max(0, newQuantity);

        if (newQuantity == 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public List<CartItemDto> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .filter(item -> item.getQuantity() > 0)
                .map(this::toDto)
                .toList();
    }

    @Override
    public Long updateQuantity(Long cartItemId, Integer quantity) {
        var item = cartItemRepository.findById(cartItemId).orElse(null);
        if(item == null)
            return null;
        item.setQuantity(quantity);
        return cartItemRepository.save(item).getId();
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppEx("User not found: " + id, HttpStatus.NOT_FOUND));
    }

    private CartItem findCartItem(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new AppEx("Cart item not found: " + id, HttpStatus.NOT_FOUND));
    }

    private CartItemDto toDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .variantId(cartItem.getVariant().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }
}

