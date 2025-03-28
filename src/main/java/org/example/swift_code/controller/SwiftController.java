package org.example.swift_code.controller;

import org.example.swift_code.model.BankBranchRequest;
import org.example.swift_code.model.BankBranchesCountryDTO;
import org.example.swift_code.service.SwiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftController {

    private final SwiftService swiftService;

    public SwiftController(SwiftService swiftService) {
        this.swiftService = swiftService;
    }

    @GetMapping(value = "/{swift-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBankBranch(@PathVariable("swift-code") String swiftCode) {
        Object bankBranch = swiftService.getBankBranch(swiftCode);
        return ResponseEntity.ok(bankBranch);
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<BankBranchesCountryDTO> getAllBankBranches(@PathVariable("countryISO2code") String countryISO2) {
        BankBranchesCountryDTO bankBranchesCountry = swiftService.getAllBankBranches(countryISO2);
        return ResponseEntity.ok(bankBranchesCountry);
    }

    @PostMapping()
    public ResponseEntity<String> createBankBranch(@RequestBody BankBranchRequest bankBranch) {
        String createdBankBranch = swiftService.createBankBranch(bankBranch);
        return new ResponseEntity<>(createdBankBranch, HttpStatus.CREATED);
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<Void> deleteBankBranch(@PathVariable("swift-code") String swiftCode) {
        swiftService.deleteBankBranch(swiftCode);
        return ResponseEntity.noContent().build();
    }
}
