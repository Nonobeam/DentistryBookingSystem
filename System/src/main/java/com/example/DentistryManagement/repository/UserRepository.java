
package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Client, String> {


    Optional<Client> findByName(String name);
    Optional<Client> findByRole(String role);
//    List<Client> findClientByRoleAAndStatus(@Param("status") int status, @Param("role") Role role);
    Client getClientsByUserID(String userId);
    @Query("SELECT c FROM Client c, Staff s " +
            "JOIN c.appointmentList a " +
            "WHERE c.role = 'CUSTOMER'  AND a.clinic.clinicID= s.clinic.clinicID AND s.staffID =: userid")
    Optional<List<Client>> getCustomersByStaff(String userid);

    Optional<List<Client>> getClientsByRoleAndDentist_Staff_UserID(Role DENTIST, String staffId);

    //boss/adminlist
    Optional<List<Client>> getClientsByRole(Role role);


    //Managerlist
    @Query("SELECT c FROM Client c , Dentist d " +
            "WHERE c.role = 'DENTIST' AND c.dentist.userID = d.dentistID and d.clinic.user.userID =: managerID")
    Optional<List<Client>> getDentistByManager(String managerID);

    @Query("SELECT c FROM Client c, Staff d " +
            "WHERE c.role = 'STAFF' AND c.staff.userID = d.staffID AND d.clinic.user.userID=:managerID")
    Optional<List<Client>> getStaffByManager(String managerID);

    boolean findClientByMailOrPhone(String mail, String phone);


//crud user
   // @Transactional
    //Client updateClientBy(Client client);

}
