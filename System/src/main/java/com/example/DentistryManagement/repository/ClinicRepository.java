package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Optional<List<Clinic>> getClinicsByUser_UserID(String managerid);

    Clinic findByClinicID(String clinicID);

    Clinic findClinicByAddressAndStatus(String address, int status);

    Optional<List<Clinic>> findClinicByUserAndStatus(Client user, int status);

    List<Clinic> findClinicByStatus(int status);

    List<Clinic> findClinicByUserMail(String mail);

//    Optional<List<Clinic>> getClinicsByUser_UserID(String managerid);

//    @Query(value = "select sc.serviceid from service_clinic sc where sc.clinicid = :clinicId")
//    List<Services> findServicesByClinicID(@Param("clinicId") String clinicID);


}
