package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String phone;
    private String mail;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate birthday;

    public UserDTO getUserDTOFromUser(Client user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setMail(user.getMail());
        userDTO.setRole(role);
        userDTO.setBirthday(birthday);
        return userDTO;
    }
}
