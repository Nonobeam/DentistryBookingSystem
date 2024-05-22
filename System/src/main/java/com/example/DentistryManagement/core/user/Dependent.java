package com.example.DentistryManagement.core.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Dependent")
@Entity
public class Dependent {
    @Id
    private String dependenceId;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
