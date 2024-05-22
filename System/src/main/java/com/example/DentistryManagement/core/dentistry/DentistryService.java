package com.example.DentistryManagement.core.dentistry;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DentistryService")
@Entity
public class DentistryService {
    @Id
    private String serviceId;
    private String name;
}
