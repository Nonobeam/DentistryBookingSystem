package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Dependent")
@Entity
public class Dependent {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dependentID")
    private String dependentID;
    private String name;
    private LocalDate birthday;

    @ManyToOne
    @JoinColumn(name = "customerID", nullable = false, referencedColumnName = "userID")
    private Client user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dependent")
    private List<Appointment> dependentList;
}
