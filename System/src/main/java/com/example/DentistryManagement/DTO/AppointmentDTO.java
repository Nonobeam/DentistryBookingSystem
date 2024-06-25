package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
public class AppointmentDTO {
    private String appointmentId;
    private int status;
    private LocalDate date;
    private Staff staff;
    private String user;
    private String dependent;
    private LocalTime timeSlot;
    private String dentist;
    private String services;
    private String clinic;
}
