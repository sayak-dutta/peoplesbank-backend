package com.example.peoplesbank.service;

import com.example.peoplesbank.dto.CustomerDto;
import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Response<CustomerDto> createCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDto customerDto = convertToDto(savedCustomer);
        return new Response<>(LocalDateTime.now(), 200,"Customer registered successfully",customerDto);
    }
    public Response<CustomerDto> loginCustomer(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            CustomerDto customerDto = convertToDto(customer);
            return new Response<>(LocalDateTime.now(), 200,"Login Successful", customerDto);
        }
        else {
            return new Response<>(LocalDateTime.now(),401,"Invalid email or password entered", null);
        }

    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setName(customerDetails.getName());
            customer.setEmail(customerDetails.getEmail());
            return customerRepository.save(customer);
        }
        return null;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        return customerDto;
    }
}
