package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<DentistSchedule, String> {
//    public Schedule getScheduleByClientAndDate(@Param("dentistID") UUID dentistID, LocalDate date);
}
