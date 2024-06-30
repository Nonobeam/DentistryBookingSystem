package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Role;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private String id;
    private String name;
    private String phone;
    private String mail;
    private LocalDate birthday;
    private Role role;
    private int status;
    private String clinicName;



}
