package org.example.swift_code.service;

import org.example.swift_code.model.*;
import org.example.swift_code.repository.BankBranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SwiftServiceTest {

    @Mock
    private BankBranchRepository bankBranchRepository;

    @InjectMocks
    private SwiftService swiftService;

    @Test
    void shouldReturnBankHeadquarter_WhenSwiftCodeExists() {
        //Given
        String swiftCode = "TESTBANKXXX";
        BankBranch bankBranch = new BankBranch(swiftCode, "Test Address", "Test Bank", "PL", "Poland", true, null);
        when(bankBranchRepository.findById(swiftCode)).thenReturn(Optional.of(bankBranch));

        //When
        BankHeadquarterDTO result = (BankHeadquarterDTO) swiftService.getBankBranch(swiftCode);

        //Then
        assertNotNull(result);
        assertEquals("TESTBANKXXX", result.swiftCode());
        assertEquals("Test Bank", result.bankName());
    }

    @Test
    void shouldReturnBankBranches_WhenSwiftCodeExists() {
        //Given
        String countryISO2 = "PL";
        BankBranch bankBranch = new BankBranch("TESTBANKXXX", "Test Address", "Test Bank", "PL", "Poland", true, null);
        BankBranch bankBranch2 = new BankBranch("TESTBWWWXXX", "Test Address 2", "Test Bank2", "PL", "Poland", true, null);
        when(bankBranchRepository.findByCountryISO2(countryISO2)).thenReturn(List.of(bankBranch, bankBranch2));

        //When
        BankBranchesCountryDTO result = swiftService.getAllBankBranches(countryISO2);

        //Then
        assertNotNull(result);
        assertEquals("PL", result.countryISO2());
        assertEquals("Poland", result.countryName());
    }

    @Test
    void shouldReturnBankBranchesCountryDTO() {
        //Given
        String swiftCode = "TESTBANKTTT";
        BankBranch bankBranch = new BankBranch(swiftCode, "Test Address", "Test Bank", "PL", "Poland", false, "TESTBANKXXX");
        when(bankBranchRepository.findById(swiftCode)).thenReturn(Optional.of(bankBranch));

        //When
        SingleBankBranchDTO result = (SingleBankBranchDTO) swiftService.getBankBranch(swiftCode);

        //Then
        assertNotNull(result);
        assertEquals("TESTBANKTTT", result.swiftCode());
        assertEquals("Test Bank", result.bankName());
    }

    @Test
    void shouldThrowException_WhenSwiftCodeNotFound() {
        // Given
        String swiftCode = "NOTEXISTXXX";
        when(bankBranchRepository.findById(swiftCode)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> swiftService.getBankBranch(swiftCode));
    }
}