package com.jobis.tax.components.incom.repository;

import com.jobis.tax.components.incom.entity.UserIncomeDeduction;
import com.jobis.tax.components.incom.entity.UserTaxInformation;
import com.jobis.tax.components.user.entity.VerifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaxInformationRepository extends JpaRepository<UserTaxInformation, Long> {
    UserTaxInformation findByUserId(String userId);
}
