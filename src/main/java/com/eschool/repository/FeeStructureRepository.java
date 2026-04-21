package com.eschool.repository;

import com.eschool.entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    // Class name aur type se fees dhundne ke liye
    Optional<FeeStructure> findByClassNameAndFeeType(String className, String feeType);

    Optional<Object> findByClassNameIgnoreCaseAndFeeTypeIgnoreCase(String className, String monthly);
}