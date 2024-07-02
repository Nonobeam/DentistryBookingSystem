package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public List<TimeSlot> findByClinic(Clinic clinic) {
        return timeSlotRepository.findByClinic(clinic);
    }
}
