package com.example.DentistryManagement.service.UserService;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDentistService {

    private final UserRepository userRepository;
    private final DentistRepository dentistRepository;

    public List<Client> findAllDentist() {
        try {
            return userRepository.getClientsByRole(Role.DENTIST);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public List<Client> findDentistListByStaff(String mail) {
        try {
            return userRepository.getClientsByRoleAndDentist_Staff_UserMail(Role.DENTIST, mail);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list by staff: " + e.getMessage(), e);
        }
    }


    public List<Client> searchDentistByStaff(String mail, String search) {
        try {
            List<Client> dentistList = userRepository.getClientsByRoleAndDentist_Staff_UserMail(Role.DENTIST, mail);
            List<Client> searchList = new ArrayList<>();
            for (Client c : dentistList) {
                if (c.getMail().contains(search) || c.getName().contains(search)) {
                    searchList.add(c);
                }
            }
            if (!searchList.isEmpty()) {
                return searchList;
            } else return null;
        } catch (DataAccessException e) {
            // Log the database access exception
            System.err.println("Database access error: " + e.getMessage());
            throw new RuntimeException("Error occurred while fetching dentist list by staff: " + e.getMessage(), e);
        }
    }

    public List<Client> findDentistFollowSearching(String search) {
        try {
            List<Dentist> dentistsList = dentistRepository.findByClinicNameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCase(search, search, search);
            List<Client> dentistListFollowSearch = new ArrayList<>();
            for (Dentist d : dentistsList) {
                dentistListFollowSearch.add(d.getUser());
            }
            return dentistListFollowSearch;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }

    }

    public Dentist findDentistByMail(String mail) {
        try {
            // Perform necessary validation and business logic here
            return dentistRepository.findDentistByUserMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllDentistByManager(String mail) {
        try {
            return userRepository.getDentistByManager(Role.DENTIST, mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllDentistInClinic(String clinicID) {
        try {
            List<Client> dentists = new ArrayList<>();
            for (Dentist s : dentistRepository.findDentistsByClinic_ClinicID(clinicID)) {
                dentists.add(s.getUser());
            }
            return dentists;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }

    public Dentist findDentistByID(String dentistID) {
        Dentist dentist = dentistRepository.findById(dentistID).orElse(null);
        if (dentist == null) {
            throw new Error("Cannot find dentist with ID " + dentistID);
        } else {
            return dentist;
        }
    }

    public List<Dentist> findDentistListByStaff(Staff staff) {
        List<Dentist> dentistList;
        try {
            dentistList = dentistRepository.findAllByStaff_StaffID(staff.getStaffID());
            return dentistList;
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
}
