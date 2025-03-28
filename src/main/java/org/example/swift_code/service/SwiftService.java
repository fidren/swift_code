package org.example.swift_code.service;

import org.example.swift_code.model.*;
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


    public Object getBankBranch(String swiftCode) {
        BankBranch bankBranch = bankBranchRepository.findById(swiftCode).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Swift Code %s does not exist", swiftCode))
        );
        if (bankBranch.isHeadquarter()) {
            List<BankBranch> branches = bankBranchRepository.findByHeadquarterSwiftCode(swiftCode);
            List<BankBranchDTO> branchDTOS = mapBranchesToDTOList(branches);
            return mapHeadquarterToDto(bankBranch, branchDTOS);
        } else {
            return mapSingleBranchToDto(bankBranch);
        }
    }

    private Object mapSingleBranchToDto(BankBranch bankBranch) {
        return new SingleBankBranchDTO(
                bankBranch.getAddress(),
                bankBranch.getBankName(),
                bankBranch.getCountryISO2(),
                bankBranch.getCountryName(),
                bankBranch.isHeadquarter(),
                bankBranch.getSwiftCode()
        );
    }

    private Object mapHeadquarterToDto(BankBranch bankBranch, List<BankBranchDTO> branchDTOS) {
        return new BankHeadquarterDTO(
                bankBranch.getAddress(),
                bankBranch.getBankName(),
                bankBranch.getCountryISO2(),
                bankBranch.getCountryName(),
                bankBranch.isHeadquarter(),
                bankBranch.getSwiftCode(),
                branchDTOS
        );
    }

    private List<BankBranchDTO> mapBranchesToDTOList(List<BankBranch> branches) {
        return branches.stream()
                .map(branch -> new BankBranchDTO(
                        branch.getAddress(),
                        branch.getBankName(),
                        branch.getCountryISO2(),
                        branch.isHeadquarter(),
                        branch.getSwiftCode()
                )).toList();
    }

    public BankBranchesCountryDTO getAllBankBranches(String countryISO2) {
        List<BankBranch> bankBranches = bankBranchRepository.findByCountryISO2(countryISO2);
        if (bankBranches.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Country ISO2 %s does not exist", countryISO2));
        }
        String countryName = bankBranches.getFirst().getCountryName();
        List<BankBranchDTO> branchDTOS = mapBranchesToDTOList(bankBranches);

        return new BankBranchesCountryDTO(countryISO2, countryName, branchDTOS);
    }

    public String createBankBranch(BankBranchRequest bankBranch) {
        return "";
    }

    public void deleteBankBranch(String swiftCode) {
        this.bankBranchRepository.deleteById(swiftCode);
    }
}
