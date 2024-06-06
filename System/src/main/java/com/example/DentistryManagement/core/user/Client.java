package com.example.DentistryManagement.core.user;


import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.mail.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    @Column(name = "clientId")
    private String id;
    @NotBlank(message = "Firstname must not be empty")
    private String firstName;
    @NotBlank(message = "Lastname must not be empty")
    private String lastName;
    @NotBlank(message = "Phone number must not be empty")
    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    private String phone;
    @NotBlank(message = "Email must not be empty")
    private String mail;
    @NotBlank(message = "Password must not be empty")
    private String name;
    private String password;
    private String birthday;
    private int status;
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientDependent")
    private List<Dependent> dependentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientNotification")
    private List<Notification> notificationList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientAppointment")
    private List<Appointment> clientAppointmentList;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return lastName + " " + firstName; // Example: Nguyen + A
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
