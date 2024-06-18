package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.repository.ClinicRepository;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final TimeSlotRepository timeSlotRepository;

//    public Optional<List<Clinic>> findClinicByManager(String userId) {
//        try {
//            return clinicRepository.getClinicsByUser_UserID(userId);
//        } catch (Error e) {
//            throw new RuntimeException("Error occurred while fetching all users: " + e.getMessage(), e);
//        }
//    }

    public Clinic save(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public Clinic findClinicByID(String clinicID) {
        return clinicRepository.findByClinicID(clinicID);
    }

    public List<Clinic> findAllClinics() {
        int status = 1;
        List<Clinic> clinics = clinicRepository.findClinicByStatus(status);

        if (clinics.isEmpty()) {
            throw new Error("Cannot find any clinic.");
        } else {
            return clinics;
        }
    }


    public Clinic updateClinicWorkingHours(String clinicId, LocalTime newStartTime, LocalTime newEndTime, LocalTime newStartBreakTime, LocalTime newEndBreakTime) {
        Clinic clinic = clinicRepository.findById(clinicId).orElseThrow(() -> new RuntimeException("Clinic not found"));

        // Update clinic's working hours
        clinic.setOpenTime(newStartTime);
        clinic.setCloseTime(newEndTime);
        clinic.setBreakStartTime(newStartBreakTime);
        clinic.setBreakEndTime(newEndBreakTime);
        clinicRepository.save(clinic);

        // Generate new TimeSlot entries
        List<TimeSlot> newTimeSlots = generateTimeSlots(clinic);
        timeSlotRepository.saveAll(newTimeSlots);

        return clinic;
    }

    private List<TimeSlot> generateTimeSlots(Clinic clinic) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        LocalTime openTime = clinic.getOpenTime();
        LocalTime closeTime = clinic.getCloseTime();
        LocalTime breakStartTime = clinic.getBreakStartTime();
        LocalTime breakEndTime = clinic.getBreakEndTime();
        LocalTime slotDuration = clinic.getSlotDuration();

        int slotNumber = 1;
        LocalTime currentTime = openTime;

        while (currentTime.isBefore(closeTime)) {
            if (currentTime.isBefore(breakStartTime) || currentTime.isAfter(breakEndTime)) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setSlotNumber(slotNumber++);
                timeSlot.setStartTime(currentTime);
                timeSlot.setClinic(clinic);
                timeSlots.add(timeSlot);
            }
            currentTime = currentTime.plusMinutes(slotDuration.getMinute());
        }
        return timeSlots;
    }

}
