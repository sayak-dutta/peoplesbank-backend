package com.example.peoplesbank.controller;

import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {
    @Autowired
    private BeneficiaryService beneficiaryService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Beneficiary> addBeneficiary(@PathVariable Long customerId, @RequestBody Beneficiary beneficiary) {
        Beneficiary newBeneficiary = beneficiaryService.addBeneficiary(customerId, beneficiary);
        return ResponseEntity.ok(newBeneficiary);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Beneficiary>> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiariesByCustomerId(customerId);
        return ResponseEntity.ok(beneficiaries);
    }

    @GetMapping("/beneficiary/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id) {
        Beneficiary beneficiary = beneficiaryService.getBeneficiaryById(id);
        return ResponseEntity.ok(beneficiary);
    }
}
