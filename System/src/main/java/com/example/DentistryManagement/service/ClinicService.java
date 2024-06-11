package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    public Clinic save(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public Clinic findClinicByID(String clinicID) {
        return clinicRepository.findByClinicID(clinicID);
    }

//    public Optional<List<Clinic>> findClinicByManager(String userId) {
//        try {
//            return clinicRepository.getClinicsByUser(userId);
//        } catch (DataAccessException e) {
//            throw new RuntimeException("Error occurred while fetching all users: " + e.getMessage(), e);
//        }
//    }
}
