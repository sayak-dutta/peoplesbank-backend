package com.example.peoplesbank.service;

import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.model.Customer;
import com.example.peoplesbank.repository.BeneficiaryRepository;
import com.example.peoplesbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Beneficiary addBeneficiary(Long customerId, Beneficiary beneficiary) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            beneficiary.setCustomer(customer);
            return beneficiaryRepository.save(beneficiary);
        }
        return null;
    }

    public List<Beneficiary> getBeneficiariesByCustomerId(Long customerId) {
        return beneficiaryRepository.findByCustomer_Id(customerId);
    }

    public Beneficiary getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id).orElse(null);
    }
}
