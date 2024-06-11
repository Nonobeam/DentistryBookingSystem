package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public Client findUserByID(String id) {
        return userRepository.findByUserID(id);
    }
    public Client findUserByIDAndRole(String id, Role role) {
        return userRepository.findByUserIDAAndRole(id, role);
    }

    public boolean existsByPhoneOOrMail(String phone, String mail) {
        return userRepository.existsByPhoneOrMail(phone, mail);
    }
    public Client save(Client client) {
        return userRepository.save(client);
    }
}
