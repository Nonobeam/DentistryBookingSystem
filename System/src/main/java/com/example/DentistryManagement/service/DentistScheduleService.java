package com.example.DentistryManagement.service;


import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.repository.DentistScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistScheduleService {
    private final DentistScheduleRepository dentistScheduleRepository;

    public Optional<List<DentistSchedule>> getByWorkDateAndServiceAndAvailableAndClinic(LocalDate workDate, com.example.DentistryManagement.core.dentistry.Service service, int available, Clinic clinic) {
        return dentistScheduleRepository.findByWorkDateAndServiceAndAvailableAndClinic(workDate, service, available, clinic);
    }

}