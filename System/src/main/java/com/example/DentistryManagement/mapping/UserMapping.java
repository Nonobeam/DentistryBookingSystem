package com.example.DentistryManagement.mapping;

import com.example.DentistryManagement.DTO.DentistResponseDTO;
import com.example.DentistryManagement.DTO.StaffResponseDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapping {
    private final UserService userService;

    public UserDTO getUserDTOFromUser(Client user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserID());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setMail(user.getMail());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setStatus(user.getStatus());
        return userDTO;
    }

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

    public DentistResponseDTO convertToDentistDTO(Client client) {
        DentistResponseDTO dentistResponseDTO = new DentistResponseDTO();
        dentistResponseDTO.setId(client.getUserID());
        dentistResponseDTO.setName(client.getName());
        dentistResponseDTO.setPhone(client.getPhone());
        dentistResponseDTO.setMail(client.getMail());
        dentistResponseDTO.setBirthday(client.getBirthday());
        dentistResponseDTO.setStatus(client.getStatus());
        Dentist dentist = userService.findDentistByMail(client.getMail());
        dentistResponseDTO.setClinicName(dentist.getClinic().getName());

        return dentistResponseDTO;
    }

    public StaffResponseDTO convertToStaffDTO(Client client) {
        StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
        staffResponseDTO.setId(client.getUserID());
        staffResponseDTO.setName(client.getName());
        staffResponseDTO.setPhone(client.getPhone());
        staffResponseDTO.setMail(client.getMail());
        staffResponseDTO.setStatus(client.getStatus());
        staffResponseDTO.setBirthday(client.getBirthday());
        Staff staff = userService.findStaffByMail(client.getMail());
        staffResponseDTO.setClinicName(staff.getClinic().getName());

        return staffResponseDTO;
    }
}
