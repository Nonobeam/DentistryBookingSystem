package com.example.DentistryManagement.auth;

import com.example.DentistryManagement.core.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Firstname must not be empty")
    private String firstName;
    @NotBlank(message = "Lastname must not be empty")
    private String lastName;
    @NotBlank(message = "Phone number must not be empty")
    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    @Size(min = 10, max = 11, message = "Phone number cannot exceed 11 characters")
    private String phone;
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String mail;
    @NotBlank(message = "Password must not be empty")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$",
            message = "Password must be at least 8 characters and contain at least one uppercase letter and one special character")
    private String password;
    private LocalDate birthday;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int status;
}
