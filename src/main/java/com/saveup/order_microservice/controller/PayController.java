package com.saveup.order_microservice.controller;

import com.saveup.order_microservice.dto.PayDto;
import com.saveup.order_microservice.exception.ResourceNotFoundException;
import com.saveup.order_microservice.exception.ValidationException;
import com.saveup.order_microservice.model.Pay;
import com.saveup.order_microservice.repository.PayRepository;
import com.saveup.order_microservice.service.PayService;
import com.saveup.order_microservice.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/saveup/v1")
public class PayController {
    @Autowired
    private PayService payService;

    private final PayRepository payRepository;
    private final CustomerServiceImpl customerServiceImpl;

    public PayController(PayRepository payRepository, CustomerServiceImpl customerServiceImpl){
        this.payRepository = payRepository;
        this.customerServiceImpl = customerServiceImpl;
    }

    //EndPoint: localhost:8080/api/saveup/v1/pays
    //Method: GET
    @Transactional(readOnly = true)
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/pays")
    public ResponseEntity<List<Pay>> getAllPays(){
        return new ResponseEntity<List<Pay>>(payRepository.findAll(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/pays/{id}
    //Method: GET
    @Transactional(readOnly = true)
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/pays/{id}")
    public ResponseEntity<Pay> getPayById(@PathVariable("id") int id){
        Pay pay = payRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("No se encontro el producto con id: " + id));
        return new ResponseEntity<>(pay, HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/pays
    //Method: POST
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/pays")
    public ResponseEntity<Pay> createPay(@RequestBody PayDto payDto){
        //validatePay(payDto);
        return new ResponseEntity<>(payService.createPay(payDto), HttpStatus.CREATED);
    }

    //EndPoint: localhost:8080/api/saveup/v1/pays/{id}
    //Method: PUT
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/pays/{id}")
    public ResponseEntity<Object> updatePay(@PathVariable("id") int id, @RequestBody PayDto payDto){
        boolean isExist=payService.isPayExist(id);
        if(isExist){
            Pay payTemp = payRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("No se encontro el producto con id: " + id));
            validatePay(payDto);
            validateUser(payDto, payTemp.getCustomerId());
            payDto.setId(id);
            payService.updatePay(payDto);
            return new ResponseEntity<>("Pay is updated succesfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Error al actualizar el pay", HttpStatus.BAD_REQUEST);
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/pays/{id}/amount
    //Method: PUT
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/pays/{id}/amount")
    public ResponseEntity<Object> updateProductStock(@PathVariable("id") int id, @RequestBody Double newAmount) {
        boolean isExist=payService.isPayExist(id);
        if(isExist){
            Pay payTemp = payRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("No se encontro el producto con id: " + id));

            payTemp.setAmount(newAmount);
            payService.updateAmountPay(payTemp);
            return new ResponseEntity<>("Pay amount is updated succesfully", HttpStatus.OK);
        } else{
            throw new ValidationException("Error al actualizar el amount del pay");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/pays/{id}
    //Method: DELETE
    @Transactional
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/pays/{id}")
    public ResponseEntity<Object> deletePay(@PathVariable("id") int id){
        payService.deletePay(id);
        return ResponseEntity.ok("Pay deleted successfully");
    }

    private void validatePay(PayDto payDto){
        if(payDto.getPayAddress() == null || payDto.getPayAddress().trim().isEmpty()){
            throw new ValidationException("La dirección de pago es obligatoria");
        }

        if (payDto.getPayDepartment() == null || payDto.getPayDepartment().trim().isEmpty()){
            throw new ValidationException("El departamento debe ser obligatorio");
        }

        if (payDto.getPayDistrict() == null || payDto.getPayDistrict().trim().isEmpty()){
            throw new ValidationException("El distrito debe ser obligatorio");
        }

        if(payDto.getCardNumber()==null||payDto.getCardNumber().trim().isEmpty()){
            throw new ValidationException("El numero de la tarjeta es obligatorio");
        } else if(payDto.getCardNumber().length() != 16){
            throw new ValidationException("El numero de la tarjeta debe tener 16 digitos");
        }

        String phoneNumberRegex = "^9\\d{8}$";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        if (!pattern.matcher(payDto.getPhoneNumber()).matches()) {
            throw new ValidationException("El numero telefonico debe empezar con 9 y tener 9 digitos");
        } else if (payDto.getPhoneNumber() == null || payDto.getPhoneNumber().trim().isEmpty()) {
            throw new ValidationException("El numero telefonico debe ser obligatorio");
        }
    }

    private void validateUser(PayDto payDto, int id) {
        if(!Objects.equals(payDto.getCustomerName(), customerServiceImpl.getCustomerById(id).getName())) {

            throw new ValidationException("Las credenciales del usuario no coinciden");

        } else if (!Objects.equals(payDto.getCustomerLastName(), customerServiceImpl.getCustomerById(id).getLastName())) {

            throw new ValidationException("Las credenciales del usuario no coinciden");

        } else if (!Objects.equals(payDto.getPhoneNumber(), customerServiceImpl.getCustomerById(id).getPhoneNumber())) {

            throw new ValidationException("Las credenciales del usuario no coinciden");

        }
    }
}