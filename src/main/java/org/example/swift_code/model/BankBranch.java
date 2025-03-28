package org.example.swift_code.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BankBranch {
    @Id
    private String swiftCode;

    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    @Setter
    private String headquarterSwiftCode;
}