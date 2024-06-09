package com.example.peoplesbank.controller;

import com.example.peoplesbank.dto.CustomerDto;
import com.example.peoplesbank.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;



import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @PostMapping
    public ResponseEntity<Response<CustomerDto>> registerCustomer(@RequestBody Customer customer) {
        Response<CustomerDto> response = customerService.createCustomer(customer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<CustomerDto>> loginCustomer(@RequestBody Customer loginRequest) {
        Response<CustomerDto> response = customerService.loginCustomer(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


//    @PostMapping("/login")
//    public ResponseEntity<CustomerDto> loginCustomer(@RequestBody Customer loginRequest) {
//        CustomerDto customer = customerService.loginCustomer(loginRequest.getEmail(), loginRequest.getPassword());
//        if (customer != null) {
//            return ResponseEntity.ok(customer);
//        } else {
//            return ResponseEntity.status(401).body(null); // Unauthorized
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        Response<String> response = new Response<>(LocalDateTime.now(), 200, "Customer deleted successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
