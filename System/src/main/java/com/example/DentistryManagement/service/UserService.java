package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<Client> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsByPhoneOOrMail(String phone, String mail) {
        return userRepository.existsByPhoneOrMail(phone, mail);
    }

    public Optional<List<Client>> findDenByStaff(String userId) {
        try {
            return userRepository.getClientsByRoleAndDentist_Staff_UserID(Role.DENTIST, userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list by staff: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findCusinClinic(String userId) {
        try {
            return userRepository.getCustomersByStaff(userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching customer list in clinic: " + e.getMessage(), e);
        }
    }

    public Client userInfo(String id) {
        try {
            return userRepository.getClientsByUserID(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching user information: " + e.getMessage(), e);
        }
    }


    public Optional<List<Client>> findAllCus() {
        try {
            return userRepository.getClientsByRole(Role.CUSTOMER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findAllStaff() {
        try {
            return userRepository.getClientsByRole(Role.STAFF);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findAllManager() {
        try {
            return userRepository.getClientsByRole(Role.MANAGER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
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

    public Optional<List<Client>> findAllDenByClinic(String userId) {
        try {
            return userRepository.getDentistByManager(userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findAllStaffByClinic(String userId) {
        try {
            return userRepository.getStaffByManager(userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public boolean isPresentUser(String id) {
        try {
            Optional<Client> savedClient = userRepository.findById(id);
            if(savedClient.isEmpty()) return false;
            else return true;

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }

    public Client updateUser(Client newClient) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.updateClient(newClient.getUserID(),newClient.getPassword(),newClient.getRole());

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }
    public Client updateUserStatus(String id){
        try {
            // Perform necessary validation and business logic here
            return userRepository.updateClientByStatus(id);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }
}
