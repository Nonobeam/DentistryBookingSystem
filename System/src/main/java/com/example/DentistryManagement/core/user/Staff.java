package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
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
@Table(name = "Staff")
@Entity
public class Staff{
    @Id
    @JsonIgnore
    @Column(name = "staffId", columnDefinition = "uniqueidentifier")
    private String staffId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staffId", referencedColumnName = "clientId")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "clinicId_fk", nullable = false, referencedColumnName = "clinicId")
    private Clinic clinicForStaff;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staffAppointment")
    private List<Appointment> staffAppointmentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staffSupervisor")
    private List<Dentist> dentistSupervisedList;
}
