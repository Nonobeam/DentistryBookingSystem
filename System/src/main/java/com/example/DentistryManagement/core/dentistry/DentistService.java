package com.example.DentistryManagement.core.dentistry;


import com.example.DentistryManagement.core.user.Dentist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "DentistService")
@Entity
public class DentistService {
     @Id
     @JsonProperty(access = JsonProperty.Access.READ_ONLY)
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int dentistServiceId;

     @ManyToOne
     @JoinColumn(name = "dentistId_fk", referencedColumnName = "dentistId")
     private Dentist dentistService;

     @ManyToOne
     @JoinColumn(name = "serviceId_fk", referencedColumnName = "serviceId")
     private DentistryService serviceDentist;

}
