package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public Optional<TimeSlot> findTimeSlotByID(String timeSlotID) {
        return timeSlotRepository.findById(timeSlotID);
    }

    public List<TimeSlot> findByClinic(Clinic clinic) {
        return timeSlotRepository.findByClinic(clinic);
    }

    public LocalDate startUpdateTimeSlotDate(String clinicID) {
        LocalDate result;
        TimeSlot timeSlot = timeSlotRepository.findTopByClinicOrderByDateDescStartTimeDesc(clinicID, PageRequest.of(0, 1)).get(0);
        result = timeSlot.getDate();
        return result;
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
