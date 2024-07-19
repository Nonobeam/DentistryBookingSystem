package com.example.DentistryManagement.service.UserService;

import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.user.*;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;
    private final DependentRepository dependentRepository;

    public boolean existsByPhoneOrMail(String phone, String mail) {
        List<Client> clients =userRepository.findClientsByPhoneOrMail(phone, mail) ;
        if(clients.isEmpty()){
            return false;
        }else {
            for (Client user : clients ) {
                if (user.getStatus() == 1) {
                    return true;
                }
            }
        }
        return false;

    }

    public String mailExtract() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while extracting mail: " + e.getMessage(), e);
        }
    }


    public Optional<Client> isPresentUser(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }


    //----------------------------------- ALL USERS -----------------------------------
    public Client findUserByMail(String mail) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.findUserByMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching user: " + e.getMessage(), e);
        }

    }

    public Client findUserById(String customerID) {
        try {
            return userRepository.findClientsByUserID(customerID);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }
    //----------------------------------- USER INFORMATION -----------------------------------

    public void updateUser(UserDTO userDTO, Client updatedUser) {
        try {
            updatedUser.setName(userDTO.getName());
            updatedUser.setPhone(userDTO.getPhone());
            updatedUser.setBirthday(userDTO.getBirthday());
            userRepository.save(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while update  user: " + e.getMessage(), e);
        }
    }

    public void updateUserStatus(Client client, int status) {
        try {
            client.setStatus(status);
            userRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }

    public Client save(Client user) {
        return userRepository.save(user);
    }
}
