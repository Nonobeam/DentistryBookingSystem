package com.example.DentistryManagement.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlotDTO {
    private int slotNumber;
    private LocalTime startTime;

}
