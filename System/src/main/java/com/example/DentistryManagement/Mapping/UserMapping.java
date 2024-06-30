package com.example.DentistryManagement.Mapping;

import com.example.DentistryManagement.DTO.AdminDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapping {
    private final UserService userService;

    public Client mapUser(UserDTO userDTO) {
        Client client = new Client();
        client.setName(userDTO.getName());
        client.setUserID(userDTO.getId());
        if (userService.existsByPhoneOrMail(client.getPhone(), client.getMail())) {
            client.setPhone(userDTO.getPhone());
            client.setMail(userDTO.getMail());
        }
        client.setBirthday(userDTO.getBirthday());
        return client;
    }

    public Client mapUserForAdmin(AdminDTO adminDTO) {
        Client client = new Client();
        client.setName(adminDTO.getName());
        client.setPhone(adminDTO.getPhone());
        client.setMail(adminDTO.getMail());
        client.setBirthday(adminDTO.getBirthday());
        client.setStatus(adminDTO.getStatus());
        return client;
    }

}
