package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    Staff findStaffByUserMail(String mail);

    Staff findStaffByStaffID(String staffID);

<<<<<<< HEAD


=======
>>>>>>> da0cba6561488739cecc3dea681e682cabdd33c8
}
