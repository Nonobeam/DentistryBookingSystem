package com.example.DentistryManagement.DTO;

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
    private String staff;
    private String user;
    private String dependent;
    private LocalTime timeSlot;
    private String dentist;
    private String services;
    private String clinic;
}
