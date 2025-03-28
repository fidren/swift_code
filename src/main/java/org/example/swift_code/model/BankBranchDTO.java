package org.example.swift_code.model;

public record BankBranchDTO(String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode) {
}
