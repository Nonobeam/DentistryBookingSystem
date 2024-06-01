package com.example.DentistryManagement.core.user;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "[User]")
@Entity
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userID;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private String mail;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return lastName + firstName; // Example: Nguyen + A
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
