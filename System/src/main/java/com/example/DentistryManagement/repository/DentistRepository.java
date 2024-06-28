package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist, String> {
    Dentist findDentistByUserMail(String mail);

    List<Dentist> findDentistsByClinic_ClinicID(String clinicID);

    Optional<List<Dentist>> findDentistByStaffAndUser_Status(Staff staff, int status);

    Dentist findByDentistID(String dentistID);

    List<Dentist> findAllByStaff(Staff staff);

    List<Dentist> findAll();

    List<Dentist> findByClinicNameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCase(String searchClinicName, String searchWord, String searchName);

}
