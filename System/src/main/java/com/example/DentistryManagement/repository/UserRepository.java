
package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {

    Optional<Client> findByMail(String mail);
    boolean existsByPhoneOrMail(String phone, String mail);
//    List<Client> findClientByRoleAAndStatus(@Param("status") int status, @Param("role") Role role);
}
