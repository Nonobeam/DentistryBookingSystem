package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.ScheduleRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistService {
    private final UserRepository userRepository;
    private final DentistRepository dentistRepository;
    private final ScheduleRepository scheduleRepository;

    public Optional<List<Client>> getAllDentistsByStatus(int status) {
        Role role = Role.DENTIST;
        return userRepository.findClientsByRoleAndStatus(role, status);
    }

//    public Schedule getDentistSchedule(UUID dentistID, LocalDate date){
//        return scheduleRepository.getScheduleByClientAndDate(dentistID, date);
//    }
}
       