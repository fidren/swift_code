package org.example.swift_code.model;

import java.util.List;

public record BankBranchesCountryDTO(
        String countryISO2,
        String countryName,
        List<BankBranchDTO> swiftCodes
){
}
