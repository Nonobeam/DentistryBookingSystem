package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
    // Find the slot number of process update date
    @Query("SELECT t FROM TimeSlot t WHERE t.clinic.clinicID = :clinicID and t.date = :date and t.slotNumber = :slotNumber")
    List<TimeSlot> findTopBySlotNumberAndClinicAndDate(@Param("slotNumber") int slotNumber, @Param("clinicID") String clinic, @Param("date") LocalDate date, Pageable pageable);

    List<TimeSlot> findByClinicOrderByStartTimeAsc(Clinic clinic);

    @Query("SELECT t FROM TimeSlot t WHERE t.clinic.clinicID = :clinicID ORDER BY t.date DESC, t.startTime DESC")
    List<TimeSlot> findTopByClinicOrderByDateDescStartTimeDesc(@Param("clinicID") String clinicID, Pageable pageable);

    List<TimeSlot> findTimeSlotsByClinicAndDate(Clinic clinic, LocalDate date);

    @Query("SELECT DISTINCT t.date FROM TimeSlot t WHERE t.clinic = :clinic ORDER BY t.date DESC")
    List<LocalDate> findDistinctTimeSlotOrderByClinicAndDateDesc(@Param("clinic") Clinic clinic);
}
