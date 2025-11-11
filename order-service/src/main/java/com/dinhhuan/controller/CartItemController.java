package com.dinhhuan.controller;

import com.dinhhuan.dto.response.CartItemDto;
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

    @RequestMapping(value = "/{userId}/add/{variantId}", method = RequestMethod.POST)
    public ResponseEntity<?> addItemToCart(@PathVariable Long userId, @PathVariable Long variantId) {
        try {
            Long id = cartService.addItemToCart(userId, variantId);
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(List.of(Map.of("id", id)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<?> increaseQuantity(@PathVariable Long cartItemId, @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            cartService.increaseQuantity(cartItemId, quantity);
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

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCartItems(@PathVariable Long userId) {
        try {
            List<CartItemDto> items = cartService.getCartItems(userId);
            if (items == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<Map<String, Object>> data = items.stream()
                    .map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", dto.getId());
                        map.put("variantId", dto.getVariantId());
                        map.put("quantity", dto.getQuantity());
                        return map;
                    })
                    .toList();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
