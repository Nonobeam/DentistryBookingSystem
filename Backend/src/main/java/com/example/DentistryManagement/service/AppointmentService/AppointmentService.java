package com.example.DentistryManagement.service.AppointmentService;

import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public LocalDate startUpdateTimeSlotDate(String clinicID) {
        LocalDate result;
        Appointment appointment = appointmentRepository.findTopByClinicOrderByDateDescStartTimeDesc(clinicID, PageRequest.of(0, 1)).get(0);
        if(appointment!=null) result = appointment.getDate();
        else result = LocalDate.now();
        return result;
    }


    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<AppointmentDTO> appointmentDTOList(List<Appointment> appointmentList) {
        List<AppointmentDTO> appointmentDTOList;
        appointmentDTOList = appointmentList.stream()
                .map(appointmentEntity -> {
                    AppointmentDTO appointment = new AppointmentDTO();
                    appointment.setAppointmentId(appointmentEntity.getAppointmentID());
                    appointment.setServices(appointmentEntity.getServices().getName());
                    appointment.setStatus(appointmentEntity.getStatus());
                    appointment.setDate(appointmentEntity.getDate());
                    appointment.setDentist(appointmentEntity.getDentist().getUser().getName());
                    appointment.setTimeSlot(appointmentEntity.getTimeSlot().getStartTime());
                    appointment.setUser(appointmentEntity.getUser().getName());
                    if (appointmentEntity.getDependent() != null) {
                        appointment.setDependent(appointmentEntity.getDependent().getName());
                        System.out.println(appointmentEntity.getDependent().getName());
                    }
                    if (appointmentEntity.getStaff() != null) {
                        appointment.setStaff(appointmentEntity.getStaff().getUser().getName());
                    }
                    appointment.setCustomerID(appointmentEntity.getUser().getUserID());

                    return appointment;
                })
                .sorted(Comparator.comparing(AppointmentDTO::getDate))
                .toList();
        return appointmentDTOList;
    }

}
