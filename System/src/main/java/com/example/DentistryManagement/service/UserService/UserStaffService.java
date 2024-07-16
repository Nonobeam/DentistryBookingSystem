package com.example.DentistryManagement.service.UserService;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.StaffRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    public List<Client> findAllStaff() {

        try {
            return userRepository.getClientsByRole(Role.STAFF);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public Staff findStaffByMail(String mail) {
        try {
            // Perform necessary validation and business logic here
            return staffRepository.findStaffByUserMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }

    public List<Client> findStaffFollowSearching(String search) {
        try {
            List<Staff> staffList = staffRepository.findByClinic_NameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCaseAndUser_Status(search, search, search,1);
            List<Client> staffListFollowSearch = new ArrayList<>();
            for (Staff s : staffList) {
                staffListFollowSearch.add(s.getUser());
            }
            return staffListFollowSearch;

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllStaffByManager(String mail) {
        try {
            return userRepository.getStaffByManager(Role.STAFF, mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllStaffInClinic(String clinicID) {
        try {
            List<Client> staffs = new ArrayList<>();
            for (Staff s : staffRepository.findStaffsByClinic_ClinicID(clinicID)) {
                staffs.add(s.getUser());
            }
            return staffs;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }

    public Staff findStaffById(String staffID) {
        Staff staff;
        try {
            staff = staffRepository.findStaffByStaffID(staffID);
            return staff;
        } catch (Error error) {
            throw error;
        }
    }
}
