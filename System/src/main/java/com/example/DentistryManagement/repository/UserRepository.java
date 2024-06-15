package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {

    Client findByUserID(String id);
    Client findByUserIDAndRole(String id, Role role);
    Optional<Client> findByMail(String mail);
    List<Client> findClientsByRole(Role role);
    Optional<List<Client>> findClientsByRoleAndStatus(Role role, int status);
    boolean existsByPhoneOrMailAndStatus(String phone, String mail, int status);
    Optional<Client> findByFirstNameOrLastName(String firstName, String lastName);
    Client findClientsByUserID(String userId);
//    @Query("SELECT c FROM Client c, Staff s " +
//            "JOIN c.appointmentList a " +
//            "WHERE c.role = 'CUSTOMER'  AND a.clinic.clinicID= s.clinic.clinicID AND s.user.mail =: mail ")
//    Optional<List<Client>> getCustomersByStaff(String mail);
//    @Query("SELECT c from Client c,Dentist  d where c.role= :DENTIST AND c.userID=d.dentistID AND d.staff.user.mail= :staffmail and c.status=1")
//    Optional<List<Client>> findClientsByRoleAndDentist_StaffMail(Role DENTIST, String staffmail);

//    @Query("SELECT c FROM Client c , Dentist d " +
//            "WHERE c.role = 'DENTIST' AND c.dentist.userID = d.dentistID and d.clinic.user.userID = :managerID ")
//    Optional<List<Client>> getDentistByManager(String managerID);
//
//    @Query("SELECT c FROM Client c, Staff d " +
//            "WHERE c.role = 'STAFF' AND c.staff.userID = d.staffID AND d.clinic.user.userID= :managerID")
//    Optional<List<Client>> getStaffByManager(String managerID);
    boolean findClientByMailOrPhone(String mail, String phone);
    Client findClientByMail(String mail);
    boolean existsByPhoneOrMail(String phone, String mail);
}
