package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.DentistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DentistService {
    private final DentistRepository dentistRepository;


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
       