package com.jobis.tax.components.user.repository;

import com.jobis.tax.components.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    boolean existsByUserIdAndPassword(String userId, String password);
    User findByUserId(String userId);
}