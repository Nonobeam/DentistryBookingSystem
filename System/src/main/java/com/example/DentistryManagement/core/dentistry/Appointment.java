package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointmentId")
    private String appointmentId;
    private int status;
    private LocalDate date;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "staffId_fk", referencedColumnName = "staffId")
    private Staff staffAppointment;

    @ManyToOne
    @JoinColumn(name = "clientId_fk",nullable = false, referencedColumnName = "clientId")
    private Client clientAppointment;

    @ManyToOne
    @JoinColumn(name = "dependentId_fk", referencedColumnName = "dependentId")
    private Dependent dependentAppointment;

    @ManyToOne
    @JoinColumn(name = "dentistId_fk",nullable = false, referencedColumnName = "dentistId")
    private Dentist dentistAppointment;

    @ManyToOne
    @JoinColumn(name = "serviceId_fk",nullable = false, referencedColumnName = "serviceId")
    private DentistryService serviceAppointment;

    @ManyToOne
    @JoinColumn(name = "clinicId_fk",nullable = false, referencedColumnName = "clinicId")
    private Clinic clinicAppointment;

}
