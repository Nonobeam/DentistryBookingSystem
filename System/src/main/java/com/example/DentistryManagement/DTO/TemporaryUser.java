package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TemporaryUser {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "userID")
    private String userID;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private String password;
    private Role role;
    private LocalDate birthday;
    private String confirmationToken;

    // Constructors, getters, setters, etc.
}

