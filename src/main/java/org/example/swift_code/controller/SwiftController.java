package org.example.swift_code.controller;

import org.example.swift_code.model.BankBranchRequest;
import org.example.swift_code.model.BankBranchesCountryDTO;
import org.example.swift_code.model.SingleBankBranchDTO;
import org.example.swift_code.service.SwiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
    public ResponseEntity<Map<String, String>> createBankBranch(@RequestBody SingleBankBranchDTO bankBranch) {
        swiftService.createBankBranch(bankBranch);
        return ResponseEntity.ok(Map.of("message", "Bank branch successfully created!"));
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<Map<String, String>> deleteBankBranch(@PathVariable("swift-code") String swiftCode) {
        swiftService.deleteBankBranch(swiftCode);
        return ResponseEntity.ok(Map.of("message", "Bank branch successfully deleted!"));
    }
}
