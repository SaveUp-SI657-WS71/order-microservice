package com.saveup.order_microservice.repository;

import com.saveup.order_microservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    void deleteByOrderIdAndProductId(int orderId, int productId);

    void deleteByOrderId(int orderId);

}