package com.saveup.order_microservice.repository;

import com.saveup.order_microservice.model.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Integer> {

}