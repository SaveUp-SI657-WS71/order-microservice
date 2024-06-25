package com.saveup.order_microservice.service;

import com.saveup.order_microservice.dto.OrderDto;
import com.saveup.order_microservice.model.Order;

import java.util.List;

public interface OrderService {
    public abstract OrderDto createOrder(OrderDto orderDto);
    public abstract void updateOrder(OrderDto orderDto);
    public abstract void deleteOrder(int id);
    public abstract Order getOrder(int id);
    public abstract List<Order> getAllOrders();
    public abstract boolean isOrderExist(int id);
}