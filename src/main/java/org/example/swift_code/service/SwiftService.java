package org.example.swift_code.service;

import org.example.swift_code.model.BankBranch;
import org.example.swift_code.model.BankBranchRequest;
import org.example.swift_code.repository.BankBranchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SwiftService {
    private final BankBranchRepository bankBranchRepository;

    public SwiftService(BankBranchRepository bankBranchRepository) {
        this.bankBranchRepository = bankBranchRepository;
    }


    public BankBranch getBankBranch(String swiftCode) {
        BankBranch bankBranch = bankBranchRepository.findById(swiftCode).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Bank branch %s does not exist", swiftCode))
        );
        return bankBranch;
    }

    public List<String> getAllBankBranches(String countryISO2) {
        return List.of();
    }

    public String createBankBranch(BankBranchRequest bankBranch) {
        return "";
    }

    public void deleteBankBranch(String swiftCode) {
        this.bankBranchRepository.deleteById(swiftCode);
    }
}
