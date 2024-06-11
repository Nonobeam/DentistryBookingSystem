
package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {

    Client findByUserID(String id);
    Client findByUserIDAAndRole(String id, Role role);
    Optional<Client> findByMail(String mail);
    boolean existsByPhoneOrMail(String phone, String mail);

    List<Client> findAll();
}
