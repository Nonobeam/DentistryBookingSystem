package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DentistRepository extends JpaRepository<Dentist, String> {
    Dentist findDentistByUserMail(String mail);

    List<Dentist> findDentistsByClinic_ClinicID(String clinicID);

    List<Dentist> findAllByStaff_StaffID(String staff);

    List<Dentist> findAll();

    List<Dentist> findByClinicNameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCase(String search, String search1, String search2);
}
