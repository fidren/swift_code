package org.example.swift_code.model;

import java.util.List;

public record BankHeadquarterDTO(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode,
        List<BankBranchDTO> branches
) {
}
