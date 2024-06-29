package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private static final Logger LOGGER = LogManager.getLogger(ServiceService.class);
    private final DentistScheduleRepository dentistScheduleRepository;

    public List<Services> findAllServices() {
        List<Services> services;
        try {
            services = serviceRepository.findAll();
            return services;
        } catch (Error error) {
            throw new Error("Error while fetch data from JPA");
        }
    }


    public Services findServiceByID(String servicesID) {
        Services service;
        try {
            service = serviceRepository.findByServiceID(servicesID);
            return service;
        } catch (Error error) {
            throw new Error("Error while fetch data from JPA");
        }
    }

    public HashSet<Services> getServiceNotNullByDate(LocalDate bookDate, Clinic clinic) {
        List<DentistSchedule> scheduleList = dentistScheduleRepository.findDentistSchedulesByClinicAndWorkDateAndAvailable(clinic, bookDate, 1);
        HashSet<Services> servicesHashSet = new HashSet<>();
        for (DentistSchedule dentistSchedule : scheduleList) {
            servicesHashSet.addAll(dentistSchedule.getDentist().getServicesList());
        }
        return servicesHashSet;

    }
}