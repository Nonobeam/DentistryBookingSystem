package com.example.DentistryManagement.repository;

<<<<<<< HEAD
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, String> {

    @Query("SELECT s " +
            "FROM Service s " +
            "JOIN DentistSchedule ds ON s = ds.service where ds.workDate= :bookDate and ds.available=1 and ds.clinic = :clinic")
    Optional<List<Service>> getServiceNotNullByDate(LocalDate bookDate, Clinic clinic);
=======
import com.example.DentistryManagement.core.dentistry.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {

>>>>>>> main
}
