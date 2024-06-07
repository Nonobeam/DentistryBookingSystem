package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Appointment")
@Entity
public class Appointment {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointmentID")
    private String appointmentID;
    private int status;
    private LocalDate date;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "staffID", referencedColumnName = "staffID")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "customerID",nullable = false, referencedColumnName = "userID")
    private Client user;

    @ManyToOne
    @JoinColumn(name = "dependentID", referencedColumnName = "dependentID")
    private Dependent dependent;

    @ManyToOne
    @JoinColumn(name = "dentistID",nullable = false, referencedColumnName = "dentistID")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "serviceID",nullable = false, referencedColumnName = "serviceID")
    private DentistryService service;

    @ManyToOne
    @JoinColumn(name = "clinicID",nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

}
