package org.example.swift_code.repository;

import org.example.swift_code.model.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankBranchRepository extends JpaRepository<BankBranch, String> {
}
