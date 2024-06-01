package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Schedule")
@Entity
public class Schedule {
    @Id
    private UUID scheduleID;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private UUID dentistID;
}
