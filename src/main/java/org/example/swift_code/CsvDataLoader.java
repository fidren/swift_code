package org.example.swift_code;

import jakarta.annotation.PostConstruct;
import org.example.swift_code.model.BankBranch;
import org.example.swift_code.repository.BankBranchRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class CsvDataLoader {
    private static final String CSV_FILE_PATH = Paths.get(System.getProperty("user.dir"), "Interns_2025_SWIFT_CODES - Sheet1.csv").toString();

    private final BankBranchRepository bankBranchRepository;
    private final Map<String, BankBranch> swiftCodeToHeadquarterMap = new HashMap<>();

    public CsvDataLoader(BankBranchRepository bankBranchRepository) {
        this.bankBranchRepository = bankBranchRepository;
    }

    @PostConstruct
    public void loadDataFromCsvFile(){
        try(BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))){
            br.lines()
                    .skip(1)
                    .map(this::parseLineToBankBranch)
                    .forEach(bankBranchRepository::save);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BankBranch parseLineToBankBranch(String line) {
        String[] split = line.split(",");
        String swiftCode = split[1];
        boolean isHeadquarter = swiftCode.endsWith("XXX");

        BankBranch bankBranch = createBankBranch(split, swiftCode, isHeadquarter);

        if(isHeadquarter){
            registerHeadquarter(swiftCode, bankBranch);
        }else {
            assignToHeadquarter(swiftCode, bankBranch);
        }

        return bankBranch;
    }

    private void assignToHeadquarter(String swiftCode, BankBranch bankBranch) {
        String headquarterSwiftCodePrefix = swiftCode.substring(0, 8);
        BankBranch headquarters = swiftCodeToHeadquarterMap.get(headquarterSwiftCodePrefix);
        bankBranch.setHeadquarter(headquarters);

        if(headquarters != null){
            if (headquarters.getBranches() == null) {
                headquarters.setBranches(new ArrayList<>());
            }
            headquarters.getBranches().add(bankBranch);
        }
    }

    private void registerHeadquarter(String swiftCode, BankBranch bankBranch) {
        swiftCodeToHeadquarterMap.put(swiftCode.substring(0, 8), bankBranch);
    }

    private BankBranch createBankBranch(String[] split, String swiftCode, boolean isHeadquarter) {
        return BankBranch.builder()
                .swiftCode(swiftCode)
                .address(split[4])
                .bankName(split[3])
                .countryISO2(split[0])
                .countryName(split[6])
                .isHeadquarter(isHeadquarter)
                .build();
    }
}
