package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.mail.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Dentist")
@Entity
public class Dentist {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dentistId")
    private String dentistId;

    @OneToOne
    @JoinColumn(name = "clientId_fk", nullable = false, referencedColumnName = "clientId")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "staffId_fk", nullable = false, referencedColumnName = "staffId")
    private Staff staffSupervisor;

    @ManyToOne
    @JoinColumn(name = "clinicId_fk", nullable = false, referencedColumnName = "clinicId")
    private Clinic clinicForDentist;

    @ManyToOne
    @JoinColumn(name = "dentistSchedule_fk", referencedColumnName = "dentistScheduleId")
    private DentistSchedule dentistSchedule;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentistAppointment")
    private List<Appointment> dentistAppointmentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentistService")
    private List<DentistService> dentistServiceList;


}