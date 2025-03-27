package org.example.swift_code.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankBranch {
    @Id
    private String swiftCode;

    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;

    @Getter
    @Setter
    @OneToMany(mappedBy = "headquarter", fetch = FetchType.LAZY)
    private List<BankBranch> branches;

    @Setter
    @ManyToOne
    @JoinColumn(name = "headquarter_swiftCode")
    private BankBranch headquarter;
}
