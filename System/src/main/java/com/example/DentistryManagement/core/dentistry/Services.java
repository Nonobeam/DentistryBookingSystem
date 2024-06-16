package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Dentist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Services {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "serviceID")
    private String serviceID;
    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "services")
    private List<Appointment> appointmentList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "services")
    private List<DentistSchedule> dentistScheduleList;

    @JsonIgnore
    //This is the ServiceClinic table
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ServiceClinic",
            joinColumns = @JoinColumn(name = "serviceID", referencedColumnName = "serviceID"),
            inverseJoinColumns = @JoinColumn(name = "clinicID", referencedColumnName = "clinicID"))
    private List<Clinic> clinicList;

    @JsonIgnore
    @ManyToMany(mappedBy = "servicesList")
    private List<Dentist> dentistList;

}
