package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
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
    private int status;
    private LocalDate date;
    private Staff staff;
    private Client user;
    private Dependent dependent;
    private LocalTime timeSlot;
    private Dentist dentist;
    private Services services;
    private Clinic clinic;
}
