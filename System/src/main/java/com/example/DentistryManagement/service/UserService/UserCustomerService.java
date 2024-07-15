package com.example.DentistryManagement.service.UserService;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.user.*;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCustomerService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;
    private final DependentRepository dependentRepository;


    public HashSet<Client> findCustomerInClinicByStaff(String mailStaff) {
        try {
            Staff staff = staffRepository.findStaffByUserMail(mailStaff);
            List<Appointment> appointmentList = appointmentRepository.findAppointmentByClinic(staff.getClinic());
            HashSet<Client> customerList = new HashSet<>();
            for (Appointment a : appointmentList) {
                customerList.add(a.getUser());
            }
            return customerList;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching customer list in clinic: " + e.getMessage(), e);
        }
    }


    public HashSet<Client> searchCustomerInClinicByStaff(String mailStaff, String search) {
        try {
            HashSet<Client> customerList = findCustomerInClinicByStaff(mailStaff);
            HashSet<Client> searchList = new HashSet<>();
            for (Client c : customerList) {
                if (c.getMail().contains(search) || c.getName().contains(search)) {
                    searchList.add(c);
                }
            }
            return searchList;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching customer list in clinic: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllCustomer() {
        try {
            return userRepository.getClientsByRole(Role.CUSTOMER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public Dependent findDependentByDependentId(String dependentID) {
        try {
            // Perform necessary validation and business logic here
            return dependentRepository.findByDependentID(dependentID);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public List<Client> findCustomerFollowSearching(String search) {
        try {
            return userRepository.findByRoleAndNameContainingIgnoreCase(Role.CUSTOMER, search);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

}
