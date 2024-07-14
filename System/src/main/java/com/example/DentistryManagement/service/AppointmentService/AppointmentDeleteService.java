package com.example.DentistryManagement.service.AppointmentService;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.repository.DentistScheduleRepository;
import com.example.DentistryManagement.service.DentistScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentDeleteService {
    private final AppointmentAnalyticService appointmentAnalyticService;
    private final DentistScheduleService dentistScheduleService;
    private final AppointmentRepository appointmentRepository;
    private final DentistScheduleRepository dentistScheduleRepository;


    // Missing save dentist schedule with set available to 1
    public void deleteAppointment(String appointmentId) {
        Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentId);
        String dentistScheduleId = appointment.getDentistScheduleId();
        DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);

        //Check for duplicate cancelled just in case
        if (appointment.getStatus() == 0) {
            throw new Error("Appointment has already been cancelled");
        }

        appointment.setStatus(0);
        dentistSchedule.setAvailable(1);
        dentistScheduleRepository.save(dentistSchedule);
        appointmentRepository.save(appointment);
    }
}
