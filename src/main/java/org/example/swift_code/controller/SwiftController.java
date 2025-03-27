package org.example.swift_code.controller;

import org.example.swift_code.model.BankBranch;
import org.example.swift_code.model.BankBranchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftController {

    private final SwiftService swiftService;

    public SwiftController(SwiftService swiftService) {
        this.swiftService = swiftService;
    }

    @GetMapping("/{swift-code}")
    public ResponseEntity<String> getBankBranch(@PathVariable("swift-code") String swiftCode) {
        String bankBranch = swiftService.getBankBranch(swiftCode);
        return ResponseEntity.ok(bankBranch);
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<List<String>> getAllBankBranches(@PathVariable("countryISO2code") String countryISO2) {
        List<String> bankBranches = swiftService.getAllBankBranches(countryISO2);
        return ResponseEntity.ok(bankBranches);
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
