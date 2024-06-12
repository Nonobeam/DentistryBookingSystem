package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Service;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Data
public class AppointmentDTO {
    private int status;
    private LocalTime timeSlot;
    private Service service;
    private String patient;
}
