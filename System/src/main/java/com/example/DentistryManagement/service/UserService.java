package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<Client> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<List<Client>> findAllDen() {
        return userRepository.getClientsByRole(Role.DENTIST);
    }

    public Optional<List<Client>> findDenByStaff(String userid) {
        return userRepository.getClientsByRoleAndDentist_Staff_UserID(Role.DENTIST,userid);
    }

    public Optional<List<Client>> findCusinClinic(String userId) {
        return userRepository.getCustomersByStaff(userId);
    }



    public Client userInfo(String id) {
        return  userRepository.getClientsByUserID(id);

}


}
