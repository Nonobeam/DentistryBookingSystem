package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    // Find the nearest Time Slot
    public TimeSlot findNearestTimeSlot(LocalDate appointmentDate, int slotNumber, String clinicId) {
        List<TimeSlot> timeSlots = timeSlotRepository.findTimeSlotsByClinic_ClinicIDAndSlotNumber(clinicId, slotNumber);

        return timeSlots.stream()
                .filter(timeSlot -> timeSlot.getDate().isBefore(appointmentDate) || timeSlot.getDate().isEqual(appointmentDate))
                .max(Comparator.comparing(TimeSlot::getDate))
                .orElse(null);
    }

    public LocalDate findNearestTimeSlot(LocalDate appointmentDate, String clinicId) {
        List<TimeSlot> timeSlots = timeSlotRepository.findTimeSlotsByClinic_ClinicID(clinicId);

        return timeSlots.stream()
                .filter(timeSlot -> timeSlot.getDate().isBefore(appointmentDate) || timeSlot.getDate().isEqual(appointmentDate))
                .max(Comparator.comparing(TimeSlot::getDate))
                .orElse(null).getDate();
    }

        // Find the start of process update date
    public LocalDate startUpdateTimeSlotDate(String clinicID) {
        LocalDate result;
        TimeSlot timeSlot = timeSlotRepository.findTopByClinicOrderByDateDescStartTimeDesc(clinicID, PageRequest.of(0, 1)).get(0);
        result = timeSlot.getDate();
        return result;
    }

    public LocalDate getNewTimeSlot(Clinic clinic) {
        List<LocalDate> timeSlots;
        try {
            timeSlots = timeSlotRepository.findDistinctTimeSlotOrderByClinicAndDateDesc(clinic);
            return timeSlots.get(0);
        } catch (Error error) {
            throw error;
        }
    }

    public LocalDate getOldTimeSlot(Clinic clinic) {
        List<LocalDate> timeSlots;
        try {
            timeSlots = timeSlotRepository.findDistinctTimeSlotOrderByClinicAndDateDesc(clinic);
            return timeSlots.get(1);
        } catch (Error error) {
            throw error;
        }
    }

    public List<TimeSlot> getTimeSlotByDate(Clinic clinic, LocalDate date) {
        List<TimeSlot> timeSlots;
        try {
            timeSlots = timeSlotRepository.findTimeSlotsByClinicAndDate(clinic, date);
            return timeSlots;
        } catch (Error error) {
            throw error;
        }
    }

    public void createAndSaveTimeSlots(LocalDate date, Clinic clinic, LocalTime startTime, LocalTime endTime,
                                       LocalTime startBreakTime, LocalTime endBreakTime, LocalTime slotDuration) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime currentTime = startTime;
        int slotNumber = 1;

        while (currentTime.isBefore(endTime)) {
            if (!(currentTime.isAfter(startBreakTime.minusSeconds(1)) && currentTime.isBefore(endBreakTime))) {
                TimeSlot timeSlot = TimeSlot.builder()
                        .slotNumber(slotNumber++)
                        .date(date)
                        .startTime(currentTime)
                        .clinic(clinic)
                        .build();
                timeSlots.add(timeSlot);
                currentTime = currentTime
                        .plusHours(slotDuration.getHour())
                        .plusMinutes(slotDuration.getMinute())
                        .plusSeconds(slotDuration.getSecond());
                Logger.getLogger(String.valueOf(timeSlot));
            } else {
                currentTime = endBreakTime;
            }
        }

        timeSlotRepository.saveAll(timeSlots);
    }


}
