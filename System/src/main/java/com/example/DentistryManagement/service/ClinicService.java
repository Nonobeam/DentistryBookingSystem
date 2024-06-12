package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;

@Service
@RequiredArgsConstructor

public class ClinicService {
    private final ClinicRepository clinicRepository;

    public Optional<List<Clinic>> findClinicByManager(String userId) {
        try {
            return clinicRepository.getClinicsByUser_UserID(userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching all users: " + e.getMessage(), e);
        }
    }
}
