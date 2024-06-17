package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
}
