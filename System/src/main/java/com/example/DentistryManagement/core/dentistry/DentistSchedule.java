package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Dentist;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate workDate;


    @ManyToOne
    @JoinColumn(name = "dentistID", referencedColumnName = "dentistID")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "clinicID", nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "timeSlotID", nullable = false, referencedColumnName = "timeSlotID")
    private TimeSlot timeslot;

    @ManyToOne
    @JoinColumn(name = "serviceID", nullable = false, referencedColumnName = "serviceID")
    private Services services;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private int available;
}
