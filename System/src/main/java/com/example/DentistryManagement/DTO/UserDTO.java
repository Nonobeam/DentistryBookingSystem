package com.example.DentistryManagement.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String phone;
    private String mail;
    private LocalDate birthday;
    private int status;


}
