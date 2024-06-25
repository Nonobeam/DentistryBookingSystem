package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
    Optional<TimeSlot> findBySlotNumberAndClinic(int timeSlot, Clinic clinic);
}
