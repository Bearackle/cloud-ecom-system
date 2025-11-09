package com.dinhhuan.order.repository;

import com.dinhhuan.order.model.CartItem;
import com.dinhhuan.order.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"user", "productVariant"})
    List<CartItem> findByUser_Id(Long userId);

    @EntityGraph(attributePaths = {"user", "productVariant"})
    Optional<CartItem> findByUser_IdAndProductVariantId(Long userId, Long productVariantId);

    void deleteByUser_Id(Long userId);

    List<CartItem> findByUser(User user);
}

