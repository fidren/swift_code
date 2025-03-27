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
import java.nio.file.Paths;
import java.util.*;

@Component
public class CsvDataLoader {
    private static final String CSV_FILE_PATH = Paths.get(System.getProperty("user.dir"), "Interns_2025_SWIFT_CODES - Sheet1.csv").toString();

    private final BankBranchRepository bankBranchRepository;
    private final Map<String, BankBranch> swiftCodeToHeadquarterMap = new HashMap<>();
    private final List<BankBranch> branchList = new LinkedList<>();

    public CsvDataLoader(BankBranchRepository bankBranchRepository) {
        this.bankBranchRepository = bankBranchRepository;
    }

    @PostConstruct
    public void loadDataFromCsvFile(){
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_FILE_PATH)).withSkipLines(1).build()) {
            List<String[]> rows = reader.readAll();
            int count = 0;
            for (String[] row : rows) {
                BankBranch bankBranch = parseLineToBankBranch(row);
                if (bankBranch.isHeadquarter()) {
                    count++;
                    bankBranchRepository.save(bankBranch);
                } else {
                    branchList.add(bankBranch);
                }
            }
            System.out.println("wpisano headquarters " + count + " do bazy danych");
            count = 0;

            for (BankBranch branch : branchList) {
                count++;
                assignToHeadquarter(branch);
                bankBranchRepository.save(branch);
            }
            System.out.println("wpisano branch " + count + " do bazy danych");
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
        BankBranch headquarters = swiftCodeToHeadquarterMap.get(headquarterSwiftCodePrefix);
        bankBranch.setHeadquarter(headquarters);

        if(headquarters != null){
            if (headquarters.getBranches() == null) {
                headquarters.setBranches(new ArrayList<>());
            }
            headquarters.getBranches().add(bankBranch);
            System.out.println("Przypisano " + bankBranch.getSwiftCode() + " do headquarters " + headquarters.getSwiftCode());
        } else {
            System.out.println("Headquarters not found, swiftcode brancha: " + bankBranch.getSwiftCode());
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
