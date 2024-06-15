package com.example.DentistryManagement.Mapping;

import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapping {
    private final UserService userService;

    public UserMapping(UserService userService) {
        this.userService = userService;
    }

    public Client mapUser (UserDTO userDTO) {
        Client client = new Client();
        client.setFirstName(userDTO.getFirstName());
        client.setLastName(userDTO.getLastName());
        if(userService.checkExistPhoneAndMail(client.getPhone(), client.getMail())){
            client.setPhone(userDTO.getPhone());
            client.setMail(userDTO.getMail());
        }
        client.setBirthday(userDTO.getBirthday());
        return client;
    }
}
