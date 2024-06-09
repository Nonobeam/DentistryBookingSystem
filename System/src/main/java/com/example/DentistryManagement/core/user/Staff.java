package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.mail.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Staff")
@Entity
public class Staff{
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "staffID", columnDefinition = "uniqueidentifier")
    private String staffID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staffID", referencedColumnName = "userID")
    private Client user;

    @ManyToOne
    @JoinColumn(name = "clinicID", nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staff")
    private List<Appointment> appointmentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staff")
    private List<Dentist> dentistList;

}
