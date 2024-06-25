package com.saveup.order_microservice.controller;

import com.saveup.order_microservice.dto.OrderDto;
import com.saveup.order_microservice.exception.ValidationException;
import com.saveup.order_microservice.model.Order;
import com.saveup.order_microservice.repository.OrderRepository;
import com.saveup.order_microservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/saveup/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders
    //Method: GET
    @Transactional(readOnly = true)
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders(){
        return new ResponseEntity<List<Order>>(orderRepository.findAll(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders
    //Method: POST
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders/{id}
    //Method: PUT
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/orders/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable("id") int id, @RequestBody OrderDto orderDto){
        boolean isExist=orderService.isOrderExist(id);
        if(isExist){
            orderDto.setId(id);
            orderService.updateOrder(orderDto);
            return new ResponseEntity<>("Order is updated succesfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al actualizar el order");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/orders/{id}
    //Method: DELETE
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}