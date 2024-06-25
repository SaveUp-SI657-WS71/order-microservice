package com.saveup.order_microservice.service.impl;

import com.saveup.order_microservice.dto.CustomerDto;
import com.saveup.order_microservice.dto.PayDto;
import com.saveup.order_microservice.exception.ValidationException;
import com.saveup.order_microservice.model.Pay;
import com.saveup.order_microservice.repository.PayRepository;
import com.saveup.order_microservice.service.PayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;
    private final CustomerServiceImpl customerServiceImpl;
    private final CardServiceImpl cardServiceImpl;

    private final ModelMapper modelMapper;

    @Autowired
    public PayServiceImpl(PayRepository payRepository, CustomerServiceImpl customerServiceImpl, CardServiceImpl cardServiceImpl, ModelMapper modelMapper) {
        this.payRepository = payRepository;
        this.customerServiceImpl = customerServiceImpl;
        this.cardServiceImpl = cardServiceImpl;
        this.modelMapper = modelMapper;
    }

    @Override
    public Pay createPay(PayDto payDto){
        Pay pay = DtoToEntity(payDto);

        CustomerDto customerDto = customerServiceImpl.getCustomerByNameAndLastNameAndPhoneNumber(payDto.getCustomerName(), payDto.getCustomerLastName(), payDto.getPhoneNumber());
        pay.setCustomerId(customerDto.getId());

        return payRepository.save(pay);
    }

    @Override
    public void updatePay(PayDto payDto){
        Pay pay = DtoToEntity(payDto);

        CustomerDto customerDto = customerServiceImpl.getCustomerByNameAndLastNameAndPhoneNumber(payDto.getCustomerName(), payDto.getCustomerLastName(), payDto.getPhoneNumber());

        if(cardServiceImpl.getCardByCardNumberAndCustomerId(payDto.getCardNumber(), customerDto.getId()) == null)
        {
            throw new ValidationException("La tarjeta no pertenece al cliente");
        }

        pay.setCustomerId(customerDto.getId());

        payRepository.save(pay);
    }

    @Override
    public void updateAmountPay(Pay pay){
        payRepository.save(pay);
    }

    @Override
    public Pay getPay(int id){ return payRepository.findById(id).get(); }

    @Override
    public void deletePay(int id){ payRepository.deleteById(id); }

    @Override
    public List<Pay> getAllPays(){ return (List<Pay>) payRepository.findAll(); }

    @Override
    public boolean isPayExist(int id){ return payRepository.existsById(id); }

    private PayDto EntityToDto(Pay pay) { return modelMapper.map(pay, PayDto.class); }

    private Pay DtoToEntity(PayDto payDto) {
        Pay pay = modelMapper.map(payDto, Pay.class);
        pay.setDate(LocalDate.now());

        return pay;
    }
}