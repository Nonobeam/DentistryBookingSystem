package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;

    public List<Clinic> findAll() {
        try {
            return clinicRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching clinic: " + e.getMessage(), e);
        }
    }

    public List<Clinic> findAllClinicsByStatus(int status) {
        try {
            return clinicRepository.findClinicsByStatus(status);
        } catch (Error e) {
            throw e;
        }
    }

    public Clinic save(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public Clinic findClinicByID(String clinicID) {
        return clinicRepository.findByClinicID(clinicID);
    }

    public boolean checkSlotDurationValid(LocalTime slotDuration){
        LocalTime min = LocalTime.of(0,30,0);
        LocalTime max = LocalTime.of(1,30, 0);
        return !slotDuration.isBefore(min) && !slotDuration.isAfter(max);
    }

    public List<Clinic> findAllClinicsByManager(String mail) {
        return clinicRepository.findClinicByUserMail(mail);
    }


}
