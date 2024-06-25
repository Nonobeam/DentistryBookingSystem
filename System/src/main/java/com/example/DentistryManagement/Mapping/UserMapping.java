package com.example.DentistryManagement.Mapping;

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

    public Client mapUser (UserDTO userDTO) {
        Client client = new Client();
        client.setName(userDTO.getName());
        if(userService.existsByPhoneOrMail(client.getPhone(), client.getMail())){
            client.setPhone(userDTO.getPhone());
            client.setMail(userDTO.getMail());
        }
        client.setBirthday(userDTO.getBirthday());
        return client;
    }
}
