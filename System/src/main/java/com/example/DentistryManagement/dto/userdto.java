package com.example.DentistryManagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class userdto {
    private String userID;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private LocalDate birthday;
}
