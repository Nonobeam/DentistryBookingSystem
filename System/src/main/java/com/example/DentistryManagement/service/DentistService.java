package com.example.DentistryManagement.service;

import com.example.DentistryManagement.repository.ScheduleRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
       