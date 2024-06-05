
package com.example.DentistryManagement.auth;


import com.example.DentistryManagement.core.user.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private String mail;
    private String name;
    private Role role;
    private int status;
}
