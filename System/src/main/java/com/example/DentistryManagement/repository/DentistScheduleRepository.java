package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, String> {

    Optional<List<DentistSchedule>> findByWorkDateAndServicesAndAvailableAndClinic(LocalDate workDate, Services service, int available, Clinic clinic);
}
