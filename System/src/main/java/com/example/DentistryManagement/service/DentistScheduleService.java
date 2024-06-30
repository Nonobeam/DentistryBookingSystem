package com.example.DentistryManagement.service;


import com.example.DentistryManagement.DTO.AvailableSchedulesResponse;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DentistScheduleService {
    private final DentistScheduleRepository dentistScheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ServiceRepository serviceRepository;
    private final DentistRepository dentistRepository;
    private final ClinicRepository clinicRepository;

    public HashSet<DentistSchedule> getByWorkDateAndServiceAndAvailableAndClinic(LocalDate workDate, String serviceId, int available, String clinicId) {
        Services service = serviceRepository.findById(serviceId).orElse(null);
        HashSet<DentistSchedule> dentistSchedulesHashSet = new HashSet<>();
        List<DentistSchedule> dentistScheduleList = dentistScheduleRepository.findByWorkDateAndAvailableAndClinic_ClinicID(workDate, available, clinicId);
        for (DentistSchedule ds : dentistScheduleList) {
            if (ds.getDentist().getServicesList().contains(service)) {
                dentistSchedulesHashSet.add(ds);
            }
        }
        return dentistSchedulesHashSet;
    }


    public void deleteDentistSchedule(String dentistID, LocalDate workDate, int numSlot) {
        Dentist dentist = dentistRepository.findById(dentistID).orElseThrow(() -> new RuntimeException("Dentist not found"));
        dentistScheduleRepository.deleteByDentistAndWorkDateAndTimeslot_SlotNumber(dentist, workDate, numSlot);
    }


    public void setDentistSchedule(String dentistID, LocalDate startDate, LocalDate endDate, int slotNumber, String clinicID) {
        Dentist dentist = dentistRepository.findById(dentistID).orElseThrow(() -> new RuntimeException("Dentist not found"));
        Clinic clinic = clinicRepository.findById(clinicID).orElseThrow(() -> new RuntimeException("Clinic not found"));
        TimeSlot timeSlot = timeSlotRepository.findBySlotNumberAndClinic(slotNumber, clinic).orElseThrow(() -> new RuntimeException("Time slot not found"));

        List<DentistSchedule> schedules = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            DentistSchedule schedule = new DentistSchedule();
            if (!dentistScheduleRepository.existsDentistScheduleByDentist_DentistIDAndTimeslotAndWorkDate(dentistID, timeSlot, date)) {
                schedule.setDentist(dentist);
                schedule.setWorkDate(date);
                schedule.setTimeslot(timeSlot);
                schedule.setClinic(clinic);
                schedules.add(schedule);
                schedule.setAvailable(1);
            }
            date = date.plusDays(1);
        }
        dentistScheduleRepository.saveAll(schedules);
    }

    public DentistSchedule findByScheduleId(String scheduleId) {
        return dentistScheduleRepository.findByScheduleID(scheduleId);
    }

    public void setAvailableDentistSchedule(DentistSchedule dentistSchedule, int available) {
        dentistSchedule.setAvailable(available);
        dentistScheduleRepository.save(dentistSchedule);
    }

    public Optional<List<DentistSchedule>> findDentistScheduleByWorkDateAndTimeSlotAndDentist(TimeSlot timeSlot, LocalDate workDate, Dentist dentist, int status) {
        return dentistScheduleRepository.findDentistSchedulesByTimeslotAndWorkDateAndAvailableAndDentist(timeSlot, workDate, status, dentist);
    }

    public List<DentistSchedule> findDentistScheduleByWorkDate(LocalDate date, int numDay, Staff staff) {
        return dentistScheduleRepository.findDentistScheduleByWorkDateBetweenAndAvailableAndDentistStaff(date, date.plusDays(numDay), 1,staff);

    }

    public  List<DentistSchedule>  findDentistScheduleByWorkDateByDentist(LocalDate date,int numDay,Dentist dentist) {
        return dentistScheduleRepository.findDentistScheduleByWorkDateBetweenAndAvailableAndDentist(date, date.plusDays(numDay), 1,dentist);

    }

}
