package com.example.peoplesbank.controller;

import com.example.peoplesbank.dto.Response;
import com.example.peoplesbank.model.Beneficiary;
import com.example.peoplesbank.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {
    @Autowired
    private BeneficiaryService beneficiaryService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Response<Beneficiary>> addBeneficiary(@PathVariable Long customerId, @RequestBody Beneficiary beneficiary) {
        Response<Beneficiary> response = beneficiaryService.addBeneficiary(customerId, beneficiary);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Beneficiary>> updateBeneficiary(@PathVariable Long id, @RequestBody Beneficiary beneficiary) {
        Response<Beneficiary> response = beneficiaryService.updateBeneficiary(id, beneficiary);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Response<List<Beneficiary>>> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        Response<List<Beneficiary>> response = beneficiaryService.getBeneficiariesByCustomerId(customerId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/beneficiary/{id}")
    public ResponseEntity<Response<Beneficiary>> getBeneficiaryById(@PathVariable Long id) {
        Response<Beneficiary> response = beneficiaryService.getBeneficiaryById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteBeneficiary(@PathVariable Long id){
        beneficiaryService.deleteBeneficiary(id);
        Response<String> response = new Response<>(LocalDateTime.now(), 200, "Beneficiary Deleted", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
