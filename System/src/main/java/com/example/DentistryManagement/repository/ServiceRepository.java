package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, String> {

    @Query("SELECT s " +
            "FROM Services s " +
            "JOIN DentistSchedule ds ON s = ds.services where ds.workDate= :bookDate and ds.available=1 and ds.clinic = :clinic")
    List<Services> getServiceNotNullByDate(LocalDate bookDate, Clinic clinic);

    List<Services> findAllByServiceIDNotNull();

    Services findByServiceID(String serviceID);


}