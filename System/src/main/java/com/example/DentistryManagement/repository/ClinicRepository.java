package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Clinic findByClinicID(String clinicID);
    String findByAddressAndStatus(String address, int status);
}
