package com.example.DentistryManagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter

public class AdminDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private LocalDate birthday;
    private String password;
    private int status;
}
