package com.example.DentistryManagement.core.dentistry;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Schedule")
@Entity
public class Schedule {
    @Id
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
