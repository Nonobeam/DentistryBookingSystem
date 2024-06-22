package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
