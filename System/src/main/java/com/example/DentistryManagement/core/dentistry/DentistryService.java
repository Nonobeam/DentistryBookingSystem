package com.example.DentistryManagement.core.dentistry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Service")
@Entity
public class DentistryService {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "serviceId")
    private String serviceId;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceAppointment")
    private List<Appointment> serviceAppointmentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceDentistSchedule")
    private List<DentistSchedule> serviceDentistScheduleList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceDentist")
    private List<DentistService> serviceDentistList;

    //This is the ServiceClinic table
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ServiceClinic",
            joinColumns = @JoinColumn(name = "serviceId_fk", referencedColumnName = "serviceId"),
            inverseJoinColumns = @JoinColumn(name = "clinicId_fk", referencedColumnName = "clinicId"))
    private List<Clinic> clinicList;

}
