package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
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
    private String password;
    private Role role;
    private int status;
    private String clinicName;

    public AdminDTO getUserDTOFromUser(Client user) {
        AdminDTO userDTO = new AdminDTO();
        if (user != null) {
            userDTO.setName(user.getName() != null ? user.getName() : "");
            userDTO.setPhone(user.getPhone() != null ? user.getPhone() : "");
            userDTO.setMail(user.getMail() != null ? user.getMail() : "");
            userDTO.setBirthday(user.getBirthday() != null ? user.getBirthday() : LocalDate.now()); // hoặc giá trị mặc định nào đó
            userDTO.setPassword(user.getPassword() != null ? user.getPassword() : "");
        }
        return userDTO;
    }

}
