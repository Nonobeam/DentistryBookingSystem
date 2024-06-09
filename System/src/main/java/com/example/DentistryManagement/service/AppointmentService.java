package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public Optional<List<Appointment>> findApointmentclinic(String userId) {
        try {
            return appointmentRepository.getAppointmentByStaffAndClinic(userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> cusAppoint(String cusid, String staff) {
        try {
            return appointmentRepository.getAppointmentByCustomerIdAndClinic(cusid, staff);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> denAppoint(String id) {
        try {
            return appointmentRepository.getAppointmentByDentistDentistIDOrderByDateAsc(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }
}
