package com.dinhhuan.controller;

import com.dinhhuan.dto.request.CartItemRequest;
import com.dinhhuan.dto.request.QuantityRequestDto;
import com.dinhhuan.dto.response.CartItemDto;
import com.dinhhuan.model.CartItem;
import com.dinhhuan.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartItemController {
    private final CartService cartService;
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addItemToCart(@RequestHeader("X-User-Id") String userId, @RequestBody CartItemRequest cartItem) {
        cartItem.setUserId(Long.parseLong(userId));
        try {
            Long id = cartService.addItemToCart(cartItem);
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(List.of(Map.of("id", id)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<?> updateItem(@PathVariable("id") Long id, @RequestBody CartItemDto cartItem) {
        Long cartItemId = cartService.updateQuantity(id, cartItem.getQuantity());
        if(cartItemId == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("id", cartItemId), HttpStatus.OK);
    }
    @RequestMapping(value = "/{cartItemId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long cartItemId) {
        try {
            Long id = cartService.removeItemFromCart(cartItemId);
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(List.of(Map.of("id", id)));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{cartItemId}/increase", method = RequestMethod.PUT)
    public ResponseEntity<?> increaseQuantity(@PathVariable Long cartItemId, @RequestBody QuantityRequestDto body) {
        try {
            cartService.increaseQuantity(cartItemId, body.getQuantity());
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{cartItemId}/decrease", method = RequestMethod.PUT)
    public ResponseEntity<?> decreaseQuantity(@PathVariable Long cartItemId, @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            cartService.decreaseQuantity(cartItemId, quantity);
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getCartItems(@RequestHeader("X-User-Id") String userId) {
        List<CartItemDto> items = cartService.getCartItems(Long.parseLong(userId));
        if (items == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(items);
    }
}
