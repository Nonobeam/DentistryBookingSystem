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
}
