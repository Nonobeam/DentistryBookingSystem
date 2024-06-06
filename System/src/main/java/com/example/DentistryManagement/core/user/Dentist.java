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
    @Column(name = "dentistId", columnDefinition = "uniqueidentifier")
    private String dentistId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "dentistId", referencedColumnName = "clientId")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "staffId_fk", nullable = false, referencedColumnName = "staffId")
    private Staff staffSupervisor;

    @ManyToOne
    @JoinColumn(name = "clinicId_fk", nullable = false, referencedColumnName = "clinicId")
    private Clinic clinicForDentist;

    @ManyToOne
    @JoinColumn(name = "dentistScheduleId_fk", referencedColumnName = "dentistScheduleId")
    private DentistSchedule dentistSchedule;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentistService")
    private List<DentistService> dentistServiceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentistAppointment")
    private List<Appointment> dentistAppointmentList;

}