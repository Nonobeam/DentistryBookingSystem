package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<List<Client>> findAllUsers() {
        try {
            return Optional.of(userRepository.findAll());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching all users: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findAllDen() {
        try {
            return userRepository.getClientsByRole(Role.DENTIST);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
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


}
