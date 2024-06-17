package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Optional<List<Clinic>> getClinicsByUser_UserID(String managerid);
    Clinic findByClinicID(String clinicID);
    Clinic findClinicByAddressAndStatus(String address, int status);
    Optional<List<Clinic>> findClinicByUserAndStatus(Client user, int status);

    List<Clinic> findClinicByStatus(int status);
List<Clinic> findClinicByUserMail(String mail);
}
