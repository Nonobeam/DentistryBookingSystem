package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    public Optional<List<Appointment>> findApointmentclinic(String userId) {
        return  appointmentRepository.getAppointmentByStaffAndClinic(userId);
    }

    public Optional<List<Appointment>> cusAppoint(String cusid,String staff) {
        return appointmentRepository.getAppointmentByCustomerIdAndClinic(cusid, staff);
    }

    public Optional<List<Appointment>> denAppoint(String id) {
        return appointmentRepository.getAppointmentByDentistDentistIDOrderByDateAsc(id);
    }
}
