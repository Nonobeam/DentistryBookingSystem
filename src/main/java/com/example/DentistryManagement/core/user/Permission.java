package com.example.DentistryManagement.core.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    READ("read"),
    WRITE("write"),
    DELETE("delete"),
    UPDATE("update")
    ;

    @Getter
    private final String permission;
}


