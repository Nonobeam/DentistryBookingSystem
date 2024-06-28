package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {
    private TimeSlotRepository timeSlotRepository;

    public Optional<TimeSlot> findTimeSlotByID(String timeSlotID) {
        return timeSlotRepository.findById(timeSlotID);
    }

    public List<TimeSlot> findByClinic(Clinic clinic) {
        return timeSlotRepository.findByClinic(clinic);
    }
}
