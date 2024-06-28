package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    Staff findStaffByUserMail(String mail);
    List<Staff> findStaffsByClinic_ClinicID(String id);

    Staff findStaffByStaffID(String staffID);

    List<Staff> findByClinic_NameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCase(String searchClinicName, String searchWord, String searchName);

}
