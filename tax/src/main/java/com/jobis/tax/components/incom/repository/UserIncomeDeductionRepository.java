package com.jobis.tax.components.incom.repository;

import com.jobis.tax.components.incom.entity.UserIncomeDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIncomeDeductionRepository extends JpaRepository<UserIncomeDeduction, Long> {
}
