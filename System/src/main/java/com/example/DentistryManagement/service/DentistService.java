package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Schedule;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.repository.ScheduleRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DentistService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

//    public List<Client> findAllDentistsByStatus(int status, Role role) {
//        return userRepository.findClientByRoleAAndStatus(status, Role.DENTIST);
//    }

//    public Schedule getDentistSchedule(UUID dentistID, LocalDate date){
//        return scheduleRepository.getScheduleByClientAndDate(dentistID, date);
//    }
}
       