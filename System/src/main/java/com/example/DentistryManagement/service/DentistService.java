package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistService {
    private final UserRepository userRepository;
    private final DentistRepository dentistRepository;

    public Optional<List<Client>> findAllDentistsByStatus(int status) {
        Role role = Role.DENTIST;
        return userRepository.findClientsByRoleAndStatus(role, status);
    }

    public Dentist findDentistByID(String dentistID) {
        Dentist dentist = dentistRepository.findById(dentistID).orElse(null);
        if (dentist == null) {
            throw new Error("Cannot find dentist with ID " + dentistID);
        } else {
            return dentist;
        }
    }

    public List<Dentist> findDentistByStaff(Staff staff) {
        List<Dentist> dentists;

        try {
            dentists = dentistRepository.findAllByStaff(staff);
            return dentists;
        } catch (Error error) {
            throw error;
        }
    }

    public Dentist updateStaffForDentist(Staff staff, Dentist dentist) {
        try {
            dentist.setStaff(staff);
            dentistRepository.save(dentist);
            return dentist;
        } catch (Error error) {
            throw error;
        }
    }

    public Dentist save(Dentist dentist) {
        try {
            return dentistRepository.save(dentist);
        } catch (Error error) {
            throw error;
        }
    }

    public Dentist findDentistByMail(String s) {
        try {
            return dentistRepository.findDentistByUserMail(s);
        } catch (Error error) {
            throw error;
        }
    }
}
       