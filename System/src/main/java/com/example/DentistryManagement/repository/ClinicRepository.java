package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Clinic findByClinicID(String clinicID);
    String findByAddressAndStatus(String address, int status);

//    Optional<List<Clinic>> getClinicsByUser_UserID(String managerid);
//    Clinic findClinicByClinicID(String clinicid);
//    Optional<List<Clinic>> getClinicByUser(Client user);
}
