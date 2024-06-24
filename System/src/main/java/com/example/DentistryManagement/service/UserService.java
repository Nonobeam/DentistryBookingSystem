package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.*;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.DependentRepository;
import com.example.DentistryManagement.repository.StaffRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final DentistRepository dentistRepository;
    private final DependentRepository dependentRepository;



    public boolean existsByPhoneOrMail(String phone, String mail) {
        return userRepository.existsByPhoneOrMailAndStatus(phone, mail, 1);
    }


    public String mailExtract() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while extracting mail: " + e.getMessage(), e);
        }
    }


    public Optional<Client> isPresentUser(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }
    

    //----------------------------------- ALL USERS -----------------------------------
    
    public List<Client> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<List<Client>> findAllDentist() {
        try {
            return userRepository.getClientsByRole(Role.DENTIST);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public Optional<List<Client>> findDentistByStaff(String mail) {
        try {
            return userRepository.getClientsByRoleAndDentist_Staff_UserMail(Role.DENTIST, mail);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list by staff: " + e.getMessage(), e);
        }
    }


    public Optional<List<Client>> findCustomerInClinic(String mailStaff) {
        try {
            return userRepository.getCustomersByStaff(mailStaff);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching customer list in clinic: " + e.getMessage(), e);
        }
    }


    public Optional<List<Client>> findAllCustomer() {
        try {
            return userRepository.getClientsByRole(Role.CUSTOMER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public Optional<List<Client>> findAllStaff() {
        try {
            return userRepository.getClientsByRole(Role.STAFF);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public Optional<List<Client>> findAllManager() {
        try {
            return userRepository.getClientsByRole(Role.MANAGER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public Client findClientByMail(String mail) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.findClientByMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
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

    public Dentist findDentistByMail(String mail) {
        try {
            // Perform necessary validation and business logic here
            return dentistRepository.findDentistByUserMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
    }

    public Client findUserById(String customerID) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.findClientsByUserID(customerID);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
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

    public List<Client> findAllDentistByManager(String mail) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.getWorkerByManager(Role.DENTIST, mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }

    }

    public List<Client> findAllStaffByManager(String mail) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.getWorkerByManager(Role.STAFF, mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findDentistInClinic(String search) {
        try {
            return userRepository.searchClientsByRoleAndDentistClinicClinicIDOrNameOrMail(Role.DENTIST, search, search, search);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }

    }

    public Optional<List<Client>> searchManager(String search) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.searchClientByRoleAndName(Role.MANAGER, search);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> findStaffInClinic(String search) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.searchClientsByRoleAndStaffClinicClinicIDOrNameOrMail(Role.STAFF, search, search, search);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }

    public Optional<List<Client>> searchCustomerSearch(String search) {
        try {
            return userRepository.searchClientByRoleAndName(Role.CUSTOMER, search);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }


    //----------------------------------- USER INFORMATION -----------------------------------


    public Client createNewUser(Client newClient) {
        try {
            // Perform necessary validation and business logic here
            Client savedClient = userRepository.save(newClient);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }
        return null;
    }


    public Client userInfo(String id) {
        try {
            return userRepository.findByUserID(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching user information: " + e.getMessage(), e);
        }
    }


    public Client updateUser(Client newClient) {
        try {
            return userRepository.save(newClient);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while update  user: " + e.getMessage(), e);
        }
    }


    public Optional<Client> updateUserStatus(Client client, int status) {
        try {
            client.setStatus(status);
            return Optional.of(userRepository.save(client));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }
}
