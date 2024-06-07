package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Dentist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "DentistSchedule")
@Entity
public class DentistSchedule {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "scheduleID")
    private String scheduleID;
    private LocalDate dateWork;


    @ManyToOne
    @JoinColumn(name = "dentistID", referencedColumnName = "dentistID")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "clinicID", nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "timeSlotID", nullable = false, referencedColumnName = "timeSlotID")
    private Timeslot timeslot;

    @ManyToOne
    @JoinColumn(name = "serviceID", nullable = false, referencedColumnName = "serviceID")
    private DentistryService service;

    @Column(name = "available", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int available;
}
