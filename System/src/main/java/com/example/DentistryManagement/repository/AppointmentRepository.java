package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
//    @Query("SELECT a FROM Appointment a, Staff s WHERE a.clinic.clinicID=s.clinic.clinicID AND s.staffID = :staffID AND a.user.userID=:customerID")
//    Optional<List<Appointment>> findAppointmentByClinicStaff(String customerID, String staffID);
//    @Query("SELECT a FROM Appointment a WHERE a.user.userID = :customerId ORDER BY a.date DESC")
//    Optional<List<Appointment>> getAppointmentByCustomerIdOrderByDateDesc(String customerId);
////    Optional<List<Appointment>> getAppointmentByDentist_User_MailOrderByDateAsc(String dentistID);
//    Optional<List<Appointment>> getAppointmentByDentistDentistIDOrderByDateDesc(String dentistID);
// //  Optional<List<Appointment>> findAppointmentByClinicStaffListAndStaffUserMail(String staffmail);
//   Optional<List<Appointment>> getAppointmentByDentist_User_MailAnd(String dentistmail, LocalDate date,int status);
//   @Query("SELECT a FROM Appointment a, Dentist s WHERE a.clinic.clinicID=s.clinic.clinicID AND s.dentistID = :dentistID AND a.user.userID=:customerID")
//    Optional<List<Appointment>> getAppointmentByCustomerIdAndDentistUser_MailAndStatus(String customerID, String dentistID,int status);
  List<Appointment> findAppointmentByTimeSlotStartTime(LocalTime timeslot);


    Optional<List<Appointment>> findAppointmentsByDateAndStatus(LocalDate date, int status);
    Appointment findAppointmentByAppointmentID(String appointmentID);
    Optional<List<Appointment>> findAppointmentsByUserAndStatus(Client client, int status);
}
