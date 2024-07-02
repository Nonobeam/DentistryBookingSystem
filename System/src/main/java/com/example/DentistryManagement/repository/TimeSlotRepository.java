package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
    Optional<TimeSlot> findBySlotNumberAndClinicAndDate(int timeSlot, Clinic clinic, LocalDate date);
    List<TimeSlot> findByClinic(Clinic clinic);

    @Query("SELECT t FROM TimeSlot t WHERE t.clinic.clinicID = :clinicID ORDER BY t.date DESC, t.startTime DESC")
    List<TimeSlot> findTopByClinicOrderByDateDescStartTimeDesc(@Param("clinicID") String clinicID, Pageable pageable);

    List<TimeSlot> findTimeSlotsByClinicAndDate(Clinic clinic, LocalDate date);
}
