package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.mail.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.angus.mail.imap.protocol.UIDSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;
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
    private List<Notification> notificationList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dentist")
    private List<Appointment> appointmentList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "DentistService",
            joinColumns = @JoinColumn(name = "dentistID", referencedColumnName = "dentistID"),
            inverseJoinColumns = @JoinColumn(name = "serviceID", referencedColumnName = "serviceID"))
    private List<Service> serviceList;

}