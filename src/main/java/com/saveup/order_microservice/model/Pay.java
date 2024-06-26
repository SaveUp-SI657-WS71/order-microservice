package com.saveup.order_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pay")
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "customer_id", length = 20, nullable = true)
    private int customerId;

    @Column(name = "amount", length = 20, nullable = true)
    private Double amount;

    @Column(name = "card_number", length = 20, nullable = true)
    private String cardNumber;

    @Column(name = "date", length = 20, nullable = true)
    private LocalDate date;

    @Column(name = "pay_address", length = 20, nullable = true)
    private String payAddress;

    @Column(name = "pay_department", length = 20, nullable = true)
    private String payDepartment;

    @Column(name = "pay_district", length = 20, nullable = true)
    private String payDistrict;

}
