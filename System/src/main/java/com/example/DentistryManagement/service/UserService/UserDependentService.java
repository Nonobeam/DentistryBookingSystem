package com.example.DentistryManagement.service.UserService;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.repository.DependentRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDependentService {

    private final UserRepository userRepository;
    private final DependentRepository dependentRepository;

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

    public Dependent findDependentByDependentId(String dependentID) {
        try {
            // Perform necessary validation and business logic here
            return dependentRepository.findByDependentID(dependentID);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while finding user: " + e.getMessage(), e);
        }
    }
}
