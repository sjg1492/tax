package com.jobis.tax.components.incom.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "user_income_deductions")
public class UserIncomeDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "deduction_type", nullable = false)
    private String deductionType;

    @Column(name = "deduction_month", nullable = false)
    private String deductionMonth;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal deductible;


}