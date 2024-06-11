package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    @Query("SELECT a FROM Appointment a, Staff s WHERE a.clinic.clinicID=s.clinic.clinicID AND s.staffID = :staffID AND a.user.userID=:customerId")
    Optional<List<Appointment>> getAppointmentByCustomerIdAndClinic(String customerId, String staffID);
    @Query("SELECT a FROM Appointment a WHERE a.user.userID = :customerId ORDER BY a.date DESC")
    Optional<List<Appointment>> getAppointmentByCustomerIdOrderByDateDesc(String customerId);
    Optional<List<Appointment>> getAppointmentByDentistDentistIDOrderByDateAsc(String dentistID);
    Optional<List<Appointment>> getAppointmentByDentistDentistIDOrderByDateDesc(String dentistID);
    @Query("SELECT a FROM Appointment a, Staff s where a.clinic.clinicID=s.clinic.clinicID and s.staffID =: staffID")
    Optional<List<Appointment>> getAppointmentByStaffAndClinic(String staffID);
    @Query("SELECT a FROM Appointment a WHERE a.dentist.dentistID = :dentistid AND a.date = :date")
    Optional<List<Appointment>> getAppointmentByDentistAndDate(String dentistid, LocalDate date);
}
