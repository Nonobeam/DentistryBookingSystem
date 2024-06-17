package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimeSlotService {
    private TimeSlotRepository timeSlotRepository;

    public Optional<TimeSlot> findTimeSlotByID(String timeSlotID) {
        return timeSlotRepository.findById(timeSlotID);
    }
}
