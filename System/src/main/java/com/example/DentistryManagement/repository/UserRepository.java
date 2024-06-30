package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {

    Client findByUserID(String id);

    Optional<Client> findByMail(String mail);

    Optional<List<Client>> findClientsByRoleAndStatus(Role role, int status);

    boolean existsByPhoneOrMailAndStatus(String phone, String mail, int status);


    Client findClientsByUserID(String userId);


    List<Client> getClientsByRoleAndDentist_Staff_UserMail(Role DENTIST, String staffMail);

    //boss/adminList
    List<Client> getClientsByRole(Role role);


    //managerList
    @Query("SELECT c FROM Client c , Dentist d " +
            "WHERE c.role = :roleParam AND c.dentist.user.userID = d.dentistID and d.clinic.user.mail = :managerMail ")
    List<Client> getDentistByManager(@Param("roleParam") Role role, String managerMail);

    @Query("SELECT c FROM Client c , Staff d " +
            "WHERE c.role = :roleParam AND c.staff.user.userID = d.staffID and d.clinic.user.mail = :managerMail ")
    List<Client> getStaffByManager(@Param("roleParam") Role role, String managerMail);

    List<Client> findByRoleAndNameContainingIgnoreCase(Role role, String searchWord);

    Client findUserByMail(String mail);


}
