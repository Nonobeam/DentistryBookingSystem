package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private LocalDate birthday;
    private int status;
}
