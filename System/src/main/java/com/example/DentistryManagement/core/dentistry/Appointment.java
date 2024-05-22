package com.example.DentistryManagement.core.dentistry;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Appointment")
@Entity
public class Appointment {
    @Id
    private String appointmentId;
    private LocalDate date;
    private LocalTime time;
}
