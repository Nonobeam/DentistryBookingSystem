package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Schedule;
import com.example.DentistryManagement.core.user.Client;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
//    public Schedule getScheduleByClientAndDate(@Param("dentistID") UUID dentistID, LocalDate date);
}
