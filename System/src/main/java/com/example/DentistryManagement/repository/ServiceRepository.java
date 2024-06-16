package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services, String> {

//    @Query("SELECT s " +
//            "FROM Service s " +
//            "JOIN DentistSchedule ds ON s = ds.service where ds.workDate= :bookDate and ds.available=1 and ds.clinic = :clinic")
//    Optional<List<Service>> getServiceNotNullByDate(LocalDate book
    List<Services> findAllByServiceIDNotNull();

    Services findByServiceID(String serviceID);


}