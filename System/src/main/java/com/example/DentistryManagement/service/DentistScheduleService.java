package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistScheduleService {
    private final DentistScheduleRepository dentistScheduleRepository;
    private final ServiceRepository serviceRepository;
    private final DentistRepository dentistRepository;
    private final ClinicRepository clinicRepository;
    private final TimeSlotService timeSlotService;
    private final AppointmentRepository appointmentRepository;

    public HashSet<DentistSchedule> getByWorkDateAndServiceAndAvailableAndClinic(LocalDate workDate, String serviceId, int available, String clinicId) {
        Services service = serviceRepository.findById(serviceId).orElse(null);
        HashSet<DentistSchedule> dentistSchedulesHashSet = new HashSet<>();
        List<DentistSchedule> dentistScheduleList = dentistScheduleRepository.findByWorkDateAndAvailableAndClinic_ClinicIDAndTimeslot_StartTimeAfter(workDate, available, clinicId, LocalTime.now());
        for (DentistSchedule ds : dentistScheduleList) {
            if (ds.getDentist().getServicesList().contains(service)) {
                dentistSchedulesHashSet.add(ds);
            }
        }
        return dentistSchedulesHashSet;
    }


    public void deleteDentistSchedule(String scheduleID) {
        DentistSchedule dentistSchedule = dentistScheduleRepository.findById(scheduleID).orElse(null);
        if (dentistSchedule != null) {
            dentistScheduleRepository.delete(dentistSchedule);
        } else throw new IllegalArgumentException("Schedule not found");

    }


    /**
     * @param dentistID  Input Dentist dentist
     * @param startDate  Input LocalDate startDate
     * @param endDate    Input LocalDate endDate
     * @param slotNumber Input int slot (e.g: 1, 2, 3, 4.....)
     * @param clinicID   Input String clinicID
     */
    public void setDentistSchedule(String dentistID, LocalDate startDate, LocalDate endDate, int slotNumber, String clinicID) {
        Dentist dentist = dentistRepository.findById(dentistID).orElseThrow(() -> new RuntimeException("Dentist not found"));
        Clinic clinic = clinicRepository.findById(clinicID).orElseThrow(() -> new RuntimeException("Clinic not found"));

        List<DentistSchedule> schedules = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            TimeSlot timeSlot = timeSlotService.findNearestTimeSlot(date, slotNumber, clinicID);
            if (timeSlot == null) {
                throw new Error("Time slot for date " + date + "is not be set.");
            }

            DentistSchedule dentistSchedule = dentistScheduleRepository.findDentistScheduleByDentist_DentistIDAndTimeslotAndWorkDate(dentistID, timeSlot, date);
            // Find dentistSchedule if not exist create new, if exist change status to exist
            if (dentistSchedule == null) {
                DentistSchedule schedule = new DentistSchedule();
                schedule.setDentist(dentist);
                schedule.setWorkDate(date);
                schedule.setTimeslot(timeSlot);
                schedule.setClinic(clinic);
                schedule.setAvailable(1);
                schedules.add(schedule);
            } else {
                // If exist set available --> 1
                dentistSchedule.setAvailable(1);
                dentistScheduleRepository.save(dentistSchedule);
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
        return dentistScheduleRepository.findDentistScheduleByWorkDateBetweenAndAvailableAndDentistStaff(date, date.plusDays(numDay), 1, staff);

    }

    public List<DentistSchedule> findDentistScheduleByWorkDateByDentist(LocalDate date, int numDay, Dentist dentist) {
        return dentistScheduleRepository.findDentistScheduleByWorkDateBetweenAndAvailableAndDentist(date, date.plusDays(numDay), 1, dentist);

    }

    public void deleteDentistSchedulesAfterDate(LocalDate workDate, String clinicId) {
        try {
            dentistScheduleRepository.deleteByWorkDateAfterAndClinicId(workDate, clinicId);
        } catch (Error error) {
            throw error;
        }
    }

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void deletePastAvailableSchedules() {
        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();
        List<DentistSchedule> pastAvailableSchedules = dentistScheduleRepository.findDentistSchedulesByAvailableAndWorkDateIsBeforeAndTimeslot_StartTimeBefore(1, now, time);
        dentistScheduleRepository.deleteAll(pastAvailableSchedules);
    }
}
