package com.jobis.tax.components.user.repository;

import com.jobis.tax.components.user.entity.User;
import com.jobis.tax.components.user.entity.VerifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifiedUserRepository extends JpaRepository<VerifiedUser, Long> {
    boolean existsByNameAndRegNo(String name, String regNo);

}
