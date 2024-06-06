package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.mail.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "dentist_id", columnDefinition = "uniqueidentifier")
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<DentistSchedule> scheduleList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentistService")
    private List<DentistService> dentistServiceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentistAppointment")
    private List<Appointment> dentistAppointmentList;

}