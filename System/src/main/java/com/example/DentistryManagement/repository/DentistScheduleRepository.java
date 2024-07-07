package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, String> {

    List<DentistSchedule> findByWorkDateAndAvailableAndClinic_ClinicID(LocalDate workDate, int available, String clinicId);
    @Transactional
    void deleteByDentistAndWorkDateAndTimeslot_SlotNumber(Dentist dentist, LocalDate workDate, int numSlot);

    DentistSchedule findByScheduleID(String scheduleID);

    Optional<List<DentistSchedule>> findDentistSchedulesByTimeslotAndWorkDateAndAvailableAndDentist(TimeSlot timeSlot, LocalDate date, int available, Dentist dentist);

    List<DentistSchedule> findDentistSchedulesByClinicAndWorkDateAndAvailable(Clinic clinic, LocalDate workDate, int available);

    Boolean existsDentistScheduleByDentist_DentistIDAndTimeslotAndWorkDate(String dentist_dentistID, TimeSlot timeslot, LocalDate workDate);

    List<DentistSchedule> findDentistScheduleByWorkDateBetweenAndAvailableAndDentist(LocalDate startDate, LocalDate endDate, int available, Dentist dentist);

    List<DentistSchedule> findDentistScheduleByWorkDateBetweenAndAvailableAndDentistStaff(LocalDate date, LocalDate localDate, int i, Staff staff);
}
