package com.saveup.order_microservice.service.impl;

import com.saveup.order_microservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductServiceImpl {
    @Autowired
    private WebClient.Builder webClientBuilder;

    public ProductDto getProductById(int productId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/saveup/v1/products/" + productId)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}
