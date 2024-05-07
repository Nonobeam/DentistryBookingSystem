package com.example.DentistryManagement.core.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.DentistryManagement.core.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    GUEST(Set.of(null)),
    CUSTOMER(
            Set.of(
                    READ,
                    WRITE
            )
    ),
    DENTIST(
            Set.of(
                   READ,
                   WRITE
            )
    ),
    CLINIC_OWNER(
            Set.of(
                    READ,
                    WRITE,
                    UPDATE,
                    DELETE
            )
    ),
    SYSTEM_ADMINAdmin(
            Set.of(
                    READ,
                    WRITE,
                    DELETE,
                    UPDATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
