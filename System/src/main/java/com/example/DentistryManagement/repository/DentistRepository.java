package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist, String> {
    Dentist findDentistByUserMail(String mail);
    Optional<List<Dentist>>  findDentistByStaffAndUser_Status(Staff staff,int status);

    Dentist findByDentistID(String dentistID);

    List<Dentist> findAllByStaff(Staff staff);
}
