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
    @Column(name = "dentistID", columnDefinition = "uniqueidentifier")
    private String dentistID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "dentistID", referencedColumnName = "userID")
    private Client user;

    @ManyToOne
    @JoinColumn(name = "clinicID", nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<DentistSchedule> dentistScheduleList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<DentistService> dentistServiceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<Appointment> appointmentList;

}