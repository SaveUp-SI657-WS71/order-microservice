package com.saveup.order_microservice.service.impl;

import com.saveup.order_microservice.dto.CustomerDto;
import com.saveup.order_microservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CustomerServiceImpl {
    @Autowired
    private WebClient.Builder webClientBuilder;

    public CustomerDto getCustomerByNameAndLastNameAndPhoneNumber(String name, String lastName, String phoneNumber) {
        return webClientBuilder.build()
                .get()
                .uri("http://192.168.56.1:8082/api/saveup/v1/customers/name/"+ name + "/lastName/" + lastName + "/phoneNumber/" + phoneNumber)
                .retrieve()
                .bodyToMono(CustomerDto.class)
                .block();
    }

    public CustomerDto getCustomerById(int customerId) {
        return webClientBuilder.build()
                .get()
                .uri("http://192.168.56.1:8082/api/saveup/v1/customers/" + customerId)
                .retrieve()
                .bodyToMono(CustomerDto.class)
                .block();
    }
}
