package com.example.peoplesbank.service;

import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.repository.BeneficiaryRepository;
import com.example.peoplesbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Response<Beneficiary> addBeneficiary(Long customerId, Beneficiary beneficiary) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            beneficiary.setCustomer(customer);
            Beneficiary newBeneficiary = beneficiaryRepository.save(beneficiary);
            return new Response<>(LocalDateTime.now(), HttpStatus.CREATED.value(), "Beneficiary added successfully", newBeneficiary);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Customer not found", null);
    }

    public Response<Beneficiary> updateBeneficiary(Long id, Beneficiary updatedBeneficiary) {
        Optional<Beneficiary> existingBeneficiaryOpt = beneficiaryRepository.findById(id);
        if (existingBeneficiaryOpt.isPresent()) {
            Beneficiary existingBeneficiary = existingBeneficiaryOpt.get();
            existingBeneficiary.setName(updatedBeneficiary.getName());
            existingBeneficiary.setAccountNumber(updatedBeneficiary.getAccountNumber());
            existingBeneficiary.setIfscCode(updatedBeneficiary.getIfscCode());
            Beneficiary savedBeneficiary = beneficiaryRepository.save(existingBeneficiary);
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Beneficiary updated successfully", savedBeneficiary);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Beneficiary not found", null);
    }

    public Response<List<Beneficiary>> getBeneficiariesByCustomerId(Long customerId) {
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByCustomer_Id(customerId);
        if (beneficiaries != null && !beneficiaries.isEmpty()) {
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Beneficiaries retrieved successfully", beneficiaries);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "No beneficiaries found for the customer", null);
    }

    public Response<Beneficiary> getBeneficiaryById(Long id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id).orElse(null);
        if (beneficiary != null) {
            return new Response<>(LocalDateTime.now(), HttpStatus.OK.value(), "Beneficiary found", beneficiary);
        }
        return new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Beneficiary not found", null);
    }

    public void deleteBeneficiary(Long id){ beneficiaryRepository.deleteById(id);}
}
