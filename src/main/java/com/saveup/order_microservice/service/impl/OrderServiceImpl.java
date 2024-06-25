package com.saveup.order_microservice.service.impl;

import com.saveup.order_microservice.dto.OrderDto;
import com.saveup.order_microservice.exception.ResourceNotFoundException;
import com.saveup.order_microservice.model.Order;
import com.saveup.order_microservice.model.Pay;
import com.saveup.order_microservice.repository.OrderRepository;
import com.saveup.order_microservice.repository.PayRepository;
import com.saveup.order_microservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private PayRepository payRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PayRepository payRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.payRepository = payRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto){
        Order order = DtoToEntity(orderDto);
        order.setDate(LocalDate.now());

        return EntityToDto(orderRepository.save(order));
    }

    @Override
    public void updateOrder(OrderDto orderDto){
        Order order = DtoToEntity(orderDto);
        Pay pay = payRepository.findById(orderDto.getPayId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el pay con id: " + orderDto.getPayId()));
        order.setPay(pay);

        orderRepository.save(order);
    }

    @Override
    public Order getOrder(int id){ return orderRepository.findById(id).get(); }

    @Override
    public void deleteOrder(int id){ orderRepository.deleteById(id); }

    @Override
    public List<Order> getAllOrders(){ return (List<Order>) orderRepository.findAll(); }

    @Override
    public boolean isOrderExist(int id){ return orderRepository.existsById(id); }

    private OrderDto EntityToDto(Order order) { return modelMapper.map(order, OrderDto.class); }

    private Order DtoToEntity(OrderDto orderDto) { return modelMapper.map(orderDto, Order.class); }
}