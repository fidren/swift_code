package org.example.swift_code.model;

public record SingleBankBranchDTO(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode
) {
}
