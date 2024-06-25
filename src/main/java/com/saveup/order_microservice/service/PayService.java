package com.saveup.order_microservice.service;

import com.saveup.order_microservice.dto.PayDto;
import com.saveup.order_microservice.model.Pay;

import java.util.List;

public interface PayService {
    public abstract Pay createPay(PayDto payDto);
    public abstract void updatePay(PayDto payDto);
    public abstract void updateAmountPay(Pay pay);
    public abstract void deletePay(int id);
    public abstract Pay getPay(int id);
    public abstract List<Pay> getAllPays();
    public abstract boolean isPayExist(int id);
}