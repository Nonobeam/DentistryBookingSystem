package com.example.DentistryManagement.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter

public class AdminDTO {
    private String name;
    private String phone;
    private String mail;
    private LocalDate birthday;
    private String password;
    private int status;
}
