package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.DTO.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, String> {
    Optional<TemporaryUser> findByConfirmationToken(String confirmationToken);
}
