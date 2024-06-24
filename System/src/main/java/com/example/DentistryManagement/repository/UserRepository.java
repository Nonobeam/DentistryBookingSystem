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

    Client findByUserIDAndRole(String id, Role role);

    Optional<Client> findByMail(String mail);

    Optional<List<Client>> findClientsByRoleAndStatus(Role role, int status);

    boolean existsByPhoneOrMailAndStatus(String phone, String mail, int status);

    Client findClientsByUserID(String userId);

    @Query("SELECT c FROM Client c, Staff s " +
            "JOIN c.appointmentList a " +
            "WHERE c.role = 'CUSTOMER'  AND a.clinic.clinicID= s.clinic.clinicID AND s.user.mail =: mail ")
    Optional<List<Client>> getCustomersByStaff(String mail);

    List<Client> getClientsByRoleAndDentist_Staff_UserMail(Role DENTIST, String staffmail);

    //boss/adminlist
    Optional<List<Client>> getClientsByRole(Role role);

    //Managerlistit
    @Query("SELECT c FROM Client c , Dentist d " +
            "WHERE c.role = :roleParam AND c.dentist.user.userID = d.dentistID and d.clinic.user.mail = :managerMail ")
    List<Client> getWorkerByManager(@Param("roleParam") Role role, String managerMail);

    boolean findClientByMailOrPhone(String mail, String phone);

    Client findClientByMail(String mail);

    Optional<List<Client>> searchClientByRoleAndName(Role role, String search);

    Optional<List<Client>> searchClientsByRoleAndDentistClinicClinicIDOrNameOrMail(Role role, String search, String searchName, String mail);

    Optional<List<Client>> searchClientsByRoleAndStaffClinicClinicIDOrNameOrMail(Role role, String search, String search1, String search2);
}
