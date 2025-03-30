package org.example.swift_code.controller;

import org.example.swift_code.model.BankBranch;
import org.example.swift_code.repository.BankBranchRepository;
import org.example.swift_code.service.SwiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SwiftControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @BeforeEach
    void clearDatabase() {
        bankBranchRepository.deleteAll();
    }

    @Test
    void shouldReturnBankBranch_WhenSwiftCodeExists() throws Exception {
        // Given
        String swiftCode = "TESTBANKXXX";
        BankBranch bankBranch = new BankBranch(swiftCode, "Test Address", "Test Bank", "PL", "Poland", true, null);
        bankBranchRepository.save(bankBranch);

        // When & Then
        mockMvc.perform(get("/v1/swift-codes/{swift-code}", swiftCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value(swiftCode))
                .andExpect(jsonPath("$.bankName").value("Test Bank"));
    }

    @Test
    void shouldReturnAllBankBranches_WhenCountryExists() throws Exception {
        // Given
        String countryISO2 = "PL";
        BankBranch bankBranch1 = new BankBranch("BANK123", "Address 1", "Bank 1", countryISO2, "Poland", true, null);
        BankBranch bankBranch2 = new BankBranch("BANK456", "Address 2", "Bank 2", countryISO2, "Poland", true, null);
        bankBranchRepository.save(bankBranch1);
        bankBranchRepository.save(bankBranch2);

        // When & Then
        mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}", countryISO2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value(countryISO2))
                .andExpect(jsonPath("$.countryName").value("Poland"))
                .andExpect(jsonPath("$.swiftCodes").isArray())
                .andExpect(jsonPath("$.swiftCodes.length()").value(2));
    }

    @Test
    void shouldCreateBankBranch_WhenValidRequest() throws Exception {
        // Given
        String requestBody = "{ \"swiftCode\": \"NEWBANK123\", \"address\": \"New Address\", \"bankName\": \"New Bank\", \"countryISO2\": \"PL\", \"countryName\": \"Poland\", \"isHeadquarter\": false, \"headquarterSwiftCode\": \"NEWBANKXXX\" }";

        // When & Then
        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bank branch successfully created!"));

        Optional<BankBranch> savedBankBranch = bankBranchRepository.findById("NEWBANK123");
        assertTrue(savedBankBranch.isPresent());
        assertEquals("New Bank", savedBankBranch.get().getBankName());
    }

    @Test
    void shouldDeleteBankBranch_WhenValidSwiftCode() throws Exception {
        // Given
        String swiftCode = "DELETEME123";
        BankBranch bankBranch = new BankBranch(swiftCode, "Delete Address", "Delete Bank", "PL", "Poland", false, "HQ123");
        bankBranchRepository.save(bankBranch);

        // When & Then
        mockMvc.perform(delete("/v1/swift-codes/{swift-code}", swiftCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bank branch successfully deleted!"));

        Optional<BankBranch> deletedBankBranch = bankBranchRepository.findById(swiftCode);
        assertFalse(deletedBankBranch.isPresent());
    }

    @Test
    void shouldReturnNotFound_WhenSwiftCodeDoesNotExist() throws Exception {
        // Given
        String swiftCode = "NOTEXIST123";

        // When & Then
        mockMvc.perform(get("/v1/swift-codes/{swift-code}", swiftCode))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_WhenCountryDoesNotExist() throws Exception {
        // Given
        String countryISO2 = "XY";

        // When & Then
        mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}", countryISO2))
                .andExpect(status().isNotFound());
    }
}