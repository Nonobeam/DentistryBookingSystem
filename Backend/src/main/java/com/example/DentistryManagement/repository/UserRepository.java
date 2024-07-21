package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {

    Optional<Client> findByMail(String mail);


    Client findClientsByUserID(String userId);

    //boss/adminList
    List<Client> getClientsByRole(Role role);


    //managerList
    @Query("SELECT c FROM Client c , Dentist d " +
            "WHERE c.role = :roleParam AND c.dentist.user.userID = d.dentistID and d.clinic.user.mail = :managerMail and d.user.status=1")
    List<Client> getDentistByManager(@Param("roleParam") Role role, String managerMail);

    @Query("SELECT c FROM Client c , Staff d " +
            "WHERE c.role = :roleParam AND c.staff.user.userID = d.staffID and d.clinic.user.mail = :managerMail and d.user.status=1")
    List<Client> getStaffByManager(@Param("roleParam") Role role, String managerMail);

    List<Client> findByRoleAndNameContainingIgnoreCase(Role role, String searchWord);

    Client findUserByMail(String mail);

    List<Client> findClientsByPhoneOrMail(String phone, String mail);

    List<Client> getClientsByRoleAndStatusAndDentist_Staff_User_Mail(Role role,int i, String mail);
}
