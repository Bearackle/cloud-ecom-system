package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.request.CreateOrderDto;
import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.request.PaymentRequest;
import com.dinhhuan.dto.response.ItemInventoryDto;
import com.dinhhuan.dto.response.ItemResponse;
import com.dinhhuan.dto.response.OrderResponse;
import com.dinhhuan.dto.response.OrderResponseDetails;
import com.dinhhuan.enums.OrderStatus;
import com.dinhhuan.model.*;
import com.dinhhuan.producer.CreateOrderEvent;
import com.dinhhuan.repository.CartItemRepository;
import com.dinhhuan.repository.OrderRepository;
import com.dinhhuan.repository.VariantRepository;
import com.dinhhuan.service.OrderService;
import com.dinhhuan.service.PaymentClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final VariantRepository variantRepository;
    private final DefaultUidGenerator uidGenerator;
    private final CreateOrderEvent createOrderEvent;
    private final PaymentClient paymentClient;
    @Override
    public String createOrder(OrderRequest orderRequest) {
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
        createOrderEvent.sendMessage(CreateOrderDto.builder()
                        .orderId(savedOrder.getId())
                        .userId(orderRequest.getUserId())
                        .totalAmount(savedOrder.getTotalAmount())
                        .method(orderRequest.getMethod())
                        .items(order.getItems().stream().map(
                                it -> ItemInventoryDto.builder()
                                        .variantId(it.getVariant().getId())
                                        .quantity(it.getQuantity())
                                        .build()
                            ).toList()
                        )
                .build());
        return paymentClient.generatePaymentUrl(PaymentRequest.builder()
                .orderId(savedOrder.getId())
                .amount(savedOrder.getTotalAmount())
                .build()).getUrl();
    }
    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppEx("Order not found: " + id, HttpStatus.NOT_FOUND));
        return toDto(order);
    }

    @Override
    @Transactional
    public List<OrderResponse> getAllUserOrder(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
       Page<Order> orders = orderRepository.findAll(pageable);
        return orders
                .map(this::toDto);
    }
    @Override
    public OrderResponse changeStatus(Long id, Integer status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppEx("Order not found: " + id, HttpStatus.NOT_FOUND));
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return toDto(savedOrder);
    }

    @Override
    public OrderResponseDetails getOrderByIdIncluded(Long id) {
        Order order = orderRepository.findOrderDetailsIncluded(id).orElse(null);
        if(order == null) {
            return null;
        }
        return OrderResponseDetails.builder()
                .id(order.getId())
                .location(order.getAddress().getLocation())
                .email(order.getUser().getEmail())
                .phone(order.getUser().getPhone())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .note(order.getNote())
                .status(OrderStatus.fromCode(order.getStatus()))
                .items(order.getItems().stream()
                        .map(i -> ItemResponse.builder()
                                .id(i.getId())
                                .price(i.getPrice())
                                .quantity(i.getQuantity())
                                .variantId(i.getVariant().getId())
                                .orderId(order.getId())
                                .build())
                        .toList()
                ).build();
    }

    private List<Item> convertCartItemsToItems(Order order, List<CartItem> cartItems) {
        List<Item> items = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() <= 0) {
                continue;
            }

            ProductVariant variant = variantRepository.findById(cartItem.getVariant().getId()).orElse(null);
            if (variant == null) continue;
            variant.setPrice(variant.getPrice() == null ? 0 : variant.getPrice());
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
