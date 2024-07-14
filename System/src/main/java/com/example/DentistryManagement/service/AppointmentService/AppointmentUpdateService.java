package com.example.DentistryManagement.service.AppointmentService;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentUpdateService {
    private final AppointmentRepository appointmentRepository;

    public Appointment UpdateAppointment(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }
}
