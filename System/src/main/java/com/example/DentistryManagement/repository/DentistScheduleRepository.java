package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, String> {

    Optional<List<DentistSchedule>> findByWorkDateAndServicesAndAvailableAndClinic(LocalDate workDate, Services service, int available, Clinic clinic);

        void deleteByDentistAndWorkDate(Dentist dentist, LocalDate workDate);

        DentistSchedule findByScheduleID(String scheduleID);

}
