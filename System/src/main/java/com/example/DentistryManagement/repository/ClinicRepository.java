package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Clinic findByClinicID(String clinicID);
    List<Clinic> findClinicByUserMail(String mail);
    List<Clinic> findClinicsByStatus(int status);

}
