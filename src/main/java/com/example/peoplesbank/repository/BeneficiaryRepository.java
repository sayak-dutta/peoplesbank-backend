package com.example.peoplesbank.repository;

import com.example.peoplesbank.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByCustomer_Id(Long customerId);
}
