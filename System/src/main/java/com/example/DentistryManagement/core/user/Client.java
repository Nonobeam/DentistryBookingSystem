package com.example.DentistryManagement.core.user;


import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.mail.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "User")
@Entity
public class Client implements UserDetails {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "userID")
    private String userID;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate birthday;
    private int status;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Dependent> dependentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Appointment> appointmentList;

    //manager only baby
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Clinic> clinicList;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    // Identity unique user
    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
