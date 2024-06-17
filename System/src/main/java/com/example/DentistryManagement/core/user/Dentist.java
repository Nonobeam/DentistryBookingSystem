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
    @JoinColumn(name = "dentistID")
    private Client user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clinicID", nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "staffID", nullable = false, referencedColumnName = "staffID")
    private Staff staff;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<DentistSchedule> dentistScheduleList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<Notification> notificationList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<Appointment> appointmentList;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "DentistService",
            joinColumns = @JoinColumn(name = "dentistID", referencedColumnName = "dentistID"),
            inverseJoinColumns = @JoinColumn(name = "serviceID", referencedColumnName = "serviceID"))
    private List<Services> servicesList;

}