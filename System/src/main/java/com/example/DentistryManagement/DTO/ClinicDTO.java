package com.example.DentistryManagement.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class ClinicDTO {
    private String phone;
    private String address;
    private Time slotDuration;
    private Time openTime;
    private Time closeTime;
    private Time breakStartTime;
    private Time breakEndTime;
}
