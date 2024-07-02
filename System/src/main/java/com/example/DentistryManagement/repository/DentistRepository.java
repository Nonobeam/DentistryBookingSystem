package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

import java.util.List;

public interface DentistRepository extends JpaRepository<Dentist, String> {
    Dentist findDentistByUserMail(String mail);

    List<Dentist> findDentistsByClinic_ClinicIDAndStaff(String clinicID, Staff staff);

    List<Dentist> findAllByStaff(Staff staff);

    List<Dentist> findAll();

    List<Dentist> findByClinicNameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCaseAndUser_Status(String search, String search1, String search2, int i);
}
