package com.saveup.order_microservice.service;

import com.saveup.order_microservice.dto.CartDto;
import com.saveup.order_microservice.model.Cart;

import java.util.List;
import java.util.Map;

public interface CartService {
    public abstract CartDto createCart(CartDto cartDto);
    public abstract void updateCart(Cart cart);
    public abstract void deleteCart(int id);
    public abstract void deleteCartsByOrder(int orderId);
    public abstract Cart getCart(int id);
    public abstract List<CartDto> getAllCarts();
    public abstract boolean isCartExist(int id);
    public List<Map<String, Object>> getCartByOrder(int orderId);
    public abstract void deleteCartByOrderAndProduct(int orderId, int productId);
}