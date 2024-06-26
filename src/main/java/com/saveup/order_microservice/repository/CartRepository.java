package com.saveup.order_microservice.repository;

import com.saveup.order_microservice.dto.ProductDto;
import com.saveup.order_microservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    void deleteByOrderIdAndProductId(int orderId, int productId);

    void deleteByOrderId(int orderId);

}