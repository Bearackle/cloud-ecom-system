package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.response.ItemResponse;
import com.dinhhuan.dto.response.OrderResponse;
import com.dinhhuan.model.*;
import com.dinhhuan.repository.CartItemRepository;
import com.dinhhuan.repository.OrderRepository;
import com.dinhhuan.repository.VariantRepository;
import com.dinhhuan.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final VariantRepository variantRepository;
    private final DefaultUidGenerator uidGenerator;

    @Override
    public Long createOrder(OrderRequest orderRequest) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(orderRequest.getUserId());
        Order order = Order.builder()
                .id(uidGenerator.getUID())
                .user(User.builder().id(orderRequest.getUserId()).build())
                .address(Address.builder().id(orderRequest.getAddressId()).build())
                .totalAmount(0L)
                .note(orderRequest.getNote())
                .status(orderRequest.getStatus() != null ? orderRequest.getStatus() : 0)
                .items(new ArrayList<>())
                .build();
        List<Item> items = convertCartItemsToItems(order, cartItems);
        order.getItems().addAll(items);

        Long totalAmount = orderRequest.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = calculateTotalAmountFromItems(items);
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

//        cartItemRepository.deleteAll(cartItems);

        return savedOrder.getId();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findOrder(id);
        return toDto(order);
    }

    @Override
    public List<OrderResponse> getAllUserOrder(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public OrderResponse changeStatus(Long id, Integer status) {
        Order order = orderRepository.findOrder(id);
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return toDto(savedOrder);
    }

    private List<Item> convertCartItemsToItems(Order order, List<CartItem> cartItems) {
        List<Item> items = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() <= 0) {
                continue;
            }

            ProductVariant variant = variantRepository.findById(cartItem.getVariant().getId()).orElse(null);
            if (variant == null) continue;

            Long itemPrice = variant.getPrice() * cartItem.getQuantity();

            Item item = Item.builder()
                    .id(uidGenerator.getUID())
                    .order(order)
                    .variant(variant)
                    .quantity(cartItem.getQuantity())
                    .price(itemPrice)
                    .build();

            items.add(item);
        }

        return items;
    }

    private Long calculateTotalAmountFromItems(List<Item> items) {
        long total = 0;
        for (Item item : items) {
            if (item.getPrice() != null) {
                total += item.getPrice();
            }
        }
        return total;
    }

    private OrderResponse toDto(Order order) {
        OrderResponse response = OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .addressId(order.getAddress().getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .note(order.getNote())
                .status(order.getStatus())
                .items(new ArrayList<>())
                .build();

        return response;
    }

    private ItemResponse toItemDto(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .orderId(item.getOrder().getId())
                .variantId(item.getVariant().getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

}
