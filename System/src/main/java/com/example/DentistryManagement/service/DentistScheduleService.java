package com.example.DentistryManagement.service;


import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.repository.DentistScheduleRepository;
import com.example.DentistryManagement.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistScheduleService {
    private final DentistScheduleRepository dentistScheduleRepository;
    private final ServiceRepository serviceRepository;
    public Optional<List<DentistSchedule>> getByWorkDateAndServiceAndAvailableAndClinic(LocalDate workDate, Services service, int available, Clinic clinic) {
        return dentistScheduleRepository.findByWorkDateAndServicesAndAvailableAndClinic(workDate, service, available, clinic);
    }

    public List<Services> getServiceNotNullByDate(LocalDate bookDate, Clinic clinic) {
        try{
            return serviceRepository.getServiceNotNullByDate(bookDate, clinic);
        }catch (Error e){
            throw e;
        }
    }
}
