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

    Optional<Client> findByFirstNameOrLastName(String firstName, String lastName);

    Client findClientsByUserID(String userId);

    @Query("SELECT c FROM Client c, Staff s " +
            "JOIN c.appointmentList a " +
            "WHERE c.role = 'CUSTOMER'  AND a.clinic.clinicID= s.clinic.clinicID AND s.user.mail =: mail ")
    Optional<List<Client>> getCustomersByStaff(String mail);

    @Query("SELECT c FROM Client c, Staff s JOIN c.appointmentList a " +
            "WHERE c.role = 'CUSTOMER' " +
            "AND a.clinic.clinicID = s.clinic.clinicID " +
            "AND s.user.mail = :mail " +
            "AND (c.mail LIKE :search OR c.firstName LIKE :search OR c.lastName LIKE :search)")
    Optional<List<Client>> searchCustomersByStaff(@Param("mail") String mail, @Param("search") String search);
    Optional<List<Client>> getClientsByRoleAndDentist_Staff_UserMail(Role DENTIST, String staffmail);
    Optional<List<Client>> getClientsByRoleAndDentist_Staff_UserMailAndFirstNameContainingOrLastNameContaining(Role DENTIST, String staffmail,String searchfirstname, String searchlastname);

    //boss/adminlist
    Optional<List<Client>> getClientsByRole(Role role);


    //Managerlistit
    @Query("SELECT c FROM Client c , Dentist d " +
            "WHERE c.role = :roleParam AND c.dentist.user.userID = d.dentistID and d.clinic.user.mail = :managerMail ")
    List<Client> getWorkerByManager(@Param("roleParam") Role role, String managerMail);

    boolean findClientByMailOrPhone(String mail, String phone);

    Client findClientByMail(String mail);

    Optional<List<Client>> searchClientByRoleAndFirstNameOrLastName(Role role, String search, String key);

    Optional<List<Client>> searchClientsByRoleAndDentistClinicClinicIDOrFirstNameOrLastNameOrMail(Role role, String search, String searchname, String searchlast, String mail);

    Optional<List<Client>> searchClientsByRoleAndStaffClinicClinicIDOrFirstNameOrLastNameOrMail(Role role, String search, String search1, String search2, String search3);

    Client findUserByMail(String mail);
}
