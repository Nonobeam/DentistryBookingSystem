package com.example.DentistryManagement.service.UserService;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagerService {

    private final UserRepository userRepository;

    public List<Client> findAllManager() {
        try {
            return userRepository.getClientsByRole(Role.MANAGER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public List<Client> findManagerFollowSearching(String search) {
        try {
            return userRepository.findByRoleAndNameContainingIgnoreCase(Role.MANAGER, search);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

}
