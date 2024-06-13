package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Role;
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
    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    @Size(min = 10, max = 11, message = "Phone number cannot exceed 11 characters")
    private String phone;
    @Email(message = "Invalid email format")
    private String mail;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate birthday;
    private String name;
}
