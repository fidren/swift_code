package org.example.swift_code.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "headquarter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    private List<BankBranch> branches = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "headquarter_swiftCode", referencedColumnName = "swiftCode")
    private BankBranch headquarter;
}