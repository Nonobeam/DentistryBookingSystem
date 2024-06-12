package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface DentistRepository extends JpaRepository<Dentist, String> {

//    @Query("SELECT d FROM Dentist d " +
//            "JOIN DentistSchedule ds ON d.dentistID = ds.dentist " +
//            "JOIN DentistService dvs ON d.dentistID = dvs.dentistID " +
//            "JOIN Appointment a ON d.dentistID = a.dentistID " +
//            "WHERE ds.dateWork = :dateWork " +
//            "AND dvs.serviceID = :serviceID " +
//            "AND ds.clinicID = :clinicID " +
//            "AND a.timeSlotID <> :timeSlotID")
//    Optional<List<Dentist>> findAvailableDentists(
//            @Param("workDate") LocalDate workDate,
//            @Param("serviceID") String serviceID,
//            @Param("clinicID") String clinicID,
//            @Param("timeSlotID") String timeSlotID
//    );
}
