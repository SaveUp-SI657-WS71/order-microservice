package com.saveup.order_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private int points;
    private String address;
    private String department;
    private String district;
    private String password;
    private String repeatPassword;
    private String phoneNumber;
}
