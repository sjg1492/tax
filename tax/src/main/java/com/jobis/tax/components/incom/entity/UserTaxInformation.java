package com.jobis.tax.components.incom.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "user_tax_information")
public class UserTaxInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,precision = 15, scale = 2)
    private  BigDecimal totalIncome;

    @Column(name = "income_deduction",nullable = false, precision = 15, scale = 2)
    private BigDecimal incomeDeduction;

    @Column(name = "tax_credit",nullable = false, precision = 15, scale = 2)
    private BigDecimal taxCredit;

    @Column(name = "user_id",nullable = false)
    private String userId;



}