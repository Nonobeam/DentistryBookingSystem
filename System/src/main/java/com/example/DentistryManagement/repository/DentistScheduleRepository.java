package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, String> {

    Optional<List<DentistSchedule>> findByWorkDateAndServiceAndAvailableAndClinic(LocalDate workDate, Service service, int available, Clinic clinic);
}
