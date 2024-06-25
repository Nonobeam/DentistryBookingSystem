package com.example.DentistryManagement.DTO;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private String name;
    private String phone;
    private String mail;
    private LocalDate birthday;
    private String password;
    private int status;
    private String clinicName;

}
