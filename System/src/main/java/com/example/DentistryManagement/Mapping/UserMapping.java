package com.example.DentistryManagement.Mapping;

import com.example.DentistryManagement.DTO.AdminDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.UserService;
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
    public AdminDTO getUserDTOFromUser(Client user) {
        AdminDTO userDTO = new AdminDTO();
        if (user != null) {
            userDTO.setName(user.getName() != null ? user.getName() : "");
            userDTO.setPhone(user.getPhone() != null ? user.getPhone() : "");
            userDTO.setMail(user.getMail() != null ? user.getMail() : "");
            userDTO.setBirthday(user.getBirthday());
            userDTO.setStatus(user.getStatus());
            userDTO.setId(user.getUserID());
        }
        return userDTO;
    }
}
