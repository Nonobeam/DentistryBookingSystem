package com.example.DentistryManagement.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalTime;

@Getter
@Setter
public class ClinicDTO {
    private String phone;
    private String name;
    private String address;
    private LocalTime slotDuration;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
}
