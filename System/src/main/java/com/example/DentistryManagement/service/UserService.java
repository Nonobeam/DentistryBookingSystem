package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.user.*;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;
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

    public List<Client> findAllDentist() {
        try {
            return userRepository.getClientsByRole(Role.DENTIST);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public List<Client> findDentistByStaff(String mail) {
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


    public List<Client> findCustomerInClinicByStaff(String mailStaff) {
        try {
            Staff staff = staffRepository.findStaffByUserMail(mailStaff);
            List<Appointment> appointmentList = appointmentRepository.findAppointmentByClinic(staff.getClinic());
            List<Client> customerList = new ArrayList<>();
            for (Appointment a : appointmentList) {
                customerList.add(a.getUser());
            }
            return customerList;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching customer list in clinic: " + e.getMessage(), e);
        }
    }


    public List<Client> searchCustomerInClinicByStaff(String mailStaff, String search) {
        try {
            List<Client> customerList = findCustomerInClinicByStaff(mailStaff);
            List<Client> searchList = new ArrayList<>();
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


    public List<Client> findAllStaff() {

        try {
            return userRepository.getClientsByRole(Role.STAFF);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }

    public List<Client> findAllManager() {
        try {
            return userRepository.getClientsByRole(Role.MANAGER);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching dentist list: " + e.getMessage(), e);
        }
    }


    public Client findClientByMail(String mail) {
        try {
            // Perform necessary validation and business logic here
            return userRepository.findUserByMail(mail);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching user: " + e.getMessage(), e);
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
            return userRepository.getDentistByManager(Role.DENTIST, mail);

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


    public List<Client> findManagerFollowSearching(String search) {
        try {
            return userRepository.findByRoleAndNameContainingIgnoreCase(Role.MANAGER, search);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }


    public List<Client> findStaffFollowSearching(String search) {
        try {
            List<Staff> staffList = staffRepository.findByClinic_NameContainingIgnoreCaseOrUser_MailContainingIgnoreCaseOrUser_NameContainingIgnoreCase(search, search, search);
            List<Client> staffListFollowSearch = new ArrayList<>();
            for (Staff s : staffList) {
                staffListFollowSearch.add(s.getUser());
            }
            return staffListFollowSearch;

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


    //----------------------------------- USER INFORMATION -----------------------------------

    public List<Dependent> findDependentByCustomer(String mail) {
        Client customer = userRepository.findUserByMail(mail);
        return dependentRepository.findByUser(customer);
    }


    public Object saveDependent(Dependent dependent) {
        try {
            // Perform necessary validation and business logic here
            return dependentRepository.save(dependent);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while insert dependent: " + e.getMessage(), e);
        }
    }

    public void updateUser(Client newClient) {
        try {
            userRepository.save(newClient);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while update  user: " + e.getMessage(), e);
        }
    }

    public void updateUserStatus(Client client, int status) {
        try {
            client.setStatus(status);
            userRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
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

    public List<Client> findAllDentistInDentist(String clinicID) {
        try {
            List<Client> dentists = new ArrayList<>();
            for (Dentist s : dentistRepository.findDentistsByClinic_ClinicIDAndStaff(clinicID, null)) {
                dentists.add(s.getUser());
            }
            return dentists;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating new user: " + e.getMessage(), e);
        }

    }

    public Optional<Client> findByMail(String mail) {
        return userRepository.findByMail(mail);
    }

    public Client save(Client client) {
        return userRepository.save(client);
    }



}
