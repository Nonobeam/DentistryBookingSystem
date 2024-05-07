package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByName(String name);
    Optional<User> findByRole(String role);
}
