package com.example.DentistryManagement.service;


import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistScheduleService {
    private final DentistScheduleRepository dentistScheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ServiceRepository serviceRepository;
    private final DentistRepository dentistRepository;
    private final ClinicRepository clinicRepository;

    public Optional<List<DentistSchedule>> getByWorkDateAndServiceAndAvailableAndClinic(LocalDate workDate, Services service, int available, Clinic clinic) {
        return dentistScheduleRepository.findByWorkDateAndServicesAndAvailableAndClinic(workDate, service, available, clinic);
    }

//    public List<Services> getServiceNotNullByDate(LocalDate bookDate, Clinic clinic) {
//        try{
//            return serviceRepository.getServiceNotNullByDate(bookDate, clinic);
//        }catch (Error e){
//            throw e;
//        }
//    }

    public void deleteDentistSchedule(String dentistID, LocalDate workDate) {
        Dentist dentist = dentistRepository.findById(dentistID).orElseThrow(() -> new RuntimeException("Dentist not found"));
        dentistScheduleRepository.deleteByDentistAndWorkDate(dentist, workDate);
    }

    public void setDentistSchedule(String dentistID, LocalDate startDate, LocalDate endDate, String timeSlotID, String clinicID, String serviceID) {
        Dentist dentist = dentistRepository.findById(dentistID).orElseThrow(() -> new RuntimeException("Dentist not found"));
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotID).orElseThrow(() -> new RuntimeException("Time slot not found"));
        Clinic clinic = clinicRepository.findById(clinicID).orElseThrow(() -> new RuntimeException("Clinic not found"));
        Services services = serviceRepository.findById(serviceID).orElseThrow(() -> new RuntimeException("Service not found"));

        List<DentistSchedule> schedules = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            // Create and add the schedule for each day
            DentistSchedule schedule = new DentistSchedule();
            schedule.setDentist(dentist);
            schedule.setWorkDate(date);
            schedule.setTimeslot(timeSlot);
            schedule.setClinic(clinic);
            schedule.setServices(services);
            schedules.add(schedule);

            date = date.plusDays(1);
        }
        dentistScheduleRepository.saveAll(schedules);
    }
}
