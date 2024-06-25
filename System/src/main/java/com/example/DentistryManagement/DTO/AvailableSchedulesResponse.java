package com.example.DentistryManagement.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AvailableSchedulesResponse {
    private String dentistName;
    private LocalTime startTime;
}
