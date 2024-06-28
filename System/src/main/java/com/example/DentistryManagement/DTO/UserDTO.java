package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Client;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private String id;
    private String name;
    private String phone;
    private String mail;
    private LocalDate birthday;

    public UserDTO getUserDTOFromUser(Client user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserID());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setMail(user.getMail());
        userDTO.setBirthday(user.getBirthday());
        return userDTO;
    }
}
