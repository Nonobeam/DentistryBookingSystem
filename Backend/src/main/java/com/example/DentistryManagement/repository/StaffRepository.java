package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    Staff findStaffByUserMail(String mail);

    List<Staff> findStaffsByClinic_ClinicID(String id);

    Staff findStaffByStaffID(String staffID);

    List<Staff> findByClinic_NameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCaseAndUser_Status(String search, String search1, String search2, int i);
}
