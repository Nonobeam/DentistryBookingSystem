
package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {

    Optional<Client> findByName(String name);
    Optional<Client> findByRole(String role);
//    List<Client> findClientByRoleAAndStatus(@Param("status") int status, @Param("role") Role role);
}
