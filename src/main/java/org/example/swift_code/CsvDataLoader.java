package org.example.swift_code;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.example.swift_code.model.BankBranch;
import org.example.swift_code.repository.BankBranchRepository;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class CsvDataLoader {
    private static final String CSV_FILE_PATH = "/data/Interns_2025_SWIFT_CODES.csv";

    private final BankBranchRepository bankBranchRepository;
    private final Set<String> headquartersSwiftCode = new HashSet<>();
    private final List<BankBranch> branchList = new LinkedList<>();

    public CsvDataLoader(BankBranchRepository bankBranchRepository) {
        this.bankBranchRepository = bankBranchRepository;
    }

    @PostConstruct
    public void loadDataFromCsvFile(){
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_FILE_PATH)).withSkipLines(1).build()) {
            List<String[]> rows = reader.readAll();

            for (String[] row : rows) {
                BankBranch bankBranch = parseLineToBankBranch(row);
                if (bankBranch.isHeadquarter()) {
                    bankBranchRepository.save(bankBranch);
                } else {
                    branchList.add(bankBranch);
                }
            }

            for (BankBranch branch : branchList) {
                assignToHeadquarter(branch);
                bankBranchRepository.save(branch);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private BankBranch parseLineToBankBranch(String[] split) {
        String swiftCode = split[1];
        boolean isHeadquarter = swiftCode.endsWith("XXX");

        BankBranch bankBranch = createBankBranch(split, swiftCode, isHeadquarter);

        if(isHeadquarter){
            registerHeadquarter(swiftCode, bankBranch);
        }

        return bankBranch;
    }

    private void assignToHeadquarter(BankBranch bankBranch) {
        String headquarterSwiftCodePrefix = bankBranch.getSwiftCode().substring(0, 8);
        if(headquartersSwiftCode.contains(headquarterSwiftCodePrefix)){
            bankBranch.setHeadquarterSwiftCode(headquarterSwiftCodePrefix + "XXX");
        }
    }

    private void registerHeadquarter(String swiftCode, BankBranch bankBranch) {
        headquartersSwiftCode.add(swiftCode.substring(0, 8));
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
