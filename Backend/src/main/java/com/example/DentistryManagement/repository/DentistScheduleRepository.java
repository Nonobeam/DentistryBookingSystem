package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, String> {

    List<DentistSchedule> findByWorkDateAndAvailableAndClinic_ClinicIDAndTimeslot_StartTimeAfter(LocalDate workDate, int available, String clinicId, LocalTime time);

    DentistSchedule findByScheduleID(String scheduleID);

    Optional<List<DentistSchedule>> findDentistSchedulesByTimeslotAndWorkDateAndAvailableAndDentist(TimeSlot timeSlot, LocalDate date, int available, Dentist dentist);

    List<DentistSchedule> findDentistSchedulesByClinicAndWorkDateAndAvailable(Clinic clinic, LocalDate workDate, int available);

    DentistSchedule findDentistScheduleByDentist_DentistIDAndTimeslotAndWorkDate(String dentist_dentistID, TimeSlot timeslot, LocalDate workDate);

    List<DentistSchedule> findDentistScheduleByWorkDateBetweenAndAvailableAndDentist(LocalDate startDate, LocalDate endDate, int available, Dentist dentist);

    List<DentistSchedule> findDentistScheduleByWorkDateBetweenAndAvailableAndDentistStaff(LocalDate date, LocalDate localDate, int available, Staff staff);

    @Transactional
    void deleteDentistSchedulesByWorkDateAfterAndClinic_ClinicID(LocalDate workDate, String clinicId);

    List<DentistSchedule> findByWorkDateAndAvailableAndClinic_ClinicID(LocalDate workDate, int available, String clinicId);

    List<DentistSchedule> findDentistSchedulesByAvailableAndWorkDateIsBeforeAndTimeslot_StartTimeBefore(int available, LocalDate workDate, LocalTime timeslot_startTime);

    List<DentistSchedule> findDentistSchedulesByWorkDateAfterAndClinic_ClinicID(LocalDate workDate, String clinicId);
}
