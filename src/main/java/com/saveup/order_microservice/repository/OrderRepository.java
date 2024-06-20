package com.saveup.order_microservice.repository;

import com.saveup.order_microservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}