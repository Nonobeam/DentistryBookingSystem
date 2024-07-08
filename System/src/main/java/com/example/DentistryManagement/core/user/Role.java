package com.example.DentistryManagement.core.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.DentistryManagement.core.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
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
    STAFF(
            Set.of(
            READ,
            WRITE,
            UPDATE,
            DELETE
            )
    ),
    MANAGER(
            Set.of(
                    READ,
                    WRITE,
                    UPDATE,
                    DELETE
            )
    ),
    ADMIN(
            Set.of(
                    READ,
                    WRITE,
                    DELETE,
                    UPDATE
            )
    ),
    BOSS(
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
        System.out.println("Authorities for role " + this.name() + ": " + authorities);
        return authorities;
    }
}
