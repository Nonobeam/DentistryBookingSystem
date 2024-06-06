package com.example.DentistryManagement.core.user;

import com.example.DentistryManagement.core.dentistry.Clinic;
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
@Table(name = "Manager")
@Entity
public class Manager{
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "managerId", columnDefinition = "uniqueidentifier")
    private String managerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "managerId", referencedColumnName = "clientId")
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manager")
    private List<Clinic> clinicList;
}
