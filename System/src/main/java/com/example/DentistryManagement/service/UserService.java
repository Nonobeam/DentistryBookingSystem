package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<Client> findAllUsers() {
        return userRepository.findAll();
    }

    public List<Client> findAllDentists() {
        try {
            return userRepository.findClientsByRole(Role.DENTIST);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllStaffs() {
        try {
            return userRepository.findClientsByRole(Role.STAFF);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllManagers() {
        try {
            return userRepository.findClientsByRole(Role.MANAGER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public Client findUserByID(String id) {
        return userRepository.findByUserID(id);
    }
    public Client findUserByIDAndRole(String id, Role role) {
        return userRepository.findByUserIDAndRole(id, role);
    }

    public boolean existsByPhoneOrMail(String phone, String mail) {
        return userRepository.existsByPhoneOrMailAndStatus(phone, mail,1);
    }

    public Client userInfo(String id) {
        try {
            return userRepository.findClientsByUserID(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching user information: " + e.getMessage(), e);
        }
    }


    public List<Client> findAllCustomer() {
        try {
            return userRepository.findClientsByRole(Role.CUSTOMER);
        } catch (Error error) {
            throw new RuntimeException("Error" + error.getMessage());
        }
    }

    public Client save(Client client) {
        return userRepository.save(client);
    }
    public Client createNewUser(Client newClient) {
        try {
            // Perform necessary validation and business logic here
            Client savedClient = userRepository.save(newClient);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean isPresent(Client createdClient) {
        try {
            return userRepository.findClientByMailOrPhone(createdClient.getMail(),createdClient.getPhone());

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }

    public Optional<Client> isPresentUser(String id) {
        try {
            return userRepository.findById(id);


        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }

    public Client updateUser(Client newClient) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.save(newClient);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while update  user: " + e.getMessage(), e);
        }
    }
    public Optional<Client> updateUserStatus(Client client){
        try {
            // Perform necessary validation and business logic here
            return Optional.of(userRepository.save(client));

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }
    public String mailExtract() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName();
        }catch (Exception e){
            throw new RuntimeException("Error occurred while extracting mail: " + e.getMessage(), e);
        }

    }
    public Client findClientByMail(String mail){
        try {
            // Perform necessary validation and business logic here
            return userRepository.findClientByMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }

    public boolean checkExistPhoneAndMail(String phone, String mail) {
        try {
            return userRepository.existsByPhoneOrMail(phone, mail);
        } catch (Error e) {
            throw new Error(e.getMessage());
        }
    }
}
