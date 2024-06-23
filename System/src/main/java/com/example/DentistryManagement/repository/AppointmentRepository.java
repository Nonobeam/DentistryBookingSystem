package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    Optional<List<Appointment>> findAppointmentByUser_UserIDAndClinic(String customerID, Clinic clinic);

    Optional<List<Appointment>> getAppointmentByDentist_User_MailOrderByDateAsc(String dentistmail);


    Optional<List<Appointment>> findAppointmentByClinic(Clinic c);

    Optional<List<Appointment>> getAppointmentByDentist_User_MailAndDateAndStatus(String dentistmail, LocalDate date, int status);

    Optional<List<Appointment>> getAppointmentByUser_UserIDAndDentist_User_MailAndStatusOrStatus(String customerId, String dentistMail, int status, int status_);


    Optional<List<Appointment>> findAppointmentsByDateAndStatus(LocalDate date, int status);

    Appointment findAppointmentByAppointmentID(String appointmentID);

    Optional<List<Appointment>> findAppointmentsByUserAndStatus(Client client, int status);

    List<Appointment> findAppointmentsByDateBetweenAndStatusOrStatus(LocalDate startOfWeek, LocalDate endOfWeek, int status, int status2);

    Optional<List<Appointment>> searchAppointmentByDateAndUser_NameOrDependent_Name(LocalDate date, String name, String dependentName);

    List<Appointment> findAppointmentsByDateAndDentist_StaffAndStatusOrStatus(LocalDate date, Staff staff, int status, int status2);

    Optional<List<Appointment>> findAppointmentsByDateBetweenAndDentistStaffAndStatusOrStatus(LocalDate startdate, LocalDate enddate, Staff staff, int status, int status2);

    List<Appointment> findAppointmentsByDateAndStatusOrStatus(LocalDate date, int i, int i1);

    List<Appointment> findAppointmentByUser_UserID(String customerID);

    List<Appointment> findAppointmentByUser_UserIDAndDate(String userID, LocalDate date);

    List<Appointment> findAppointmentByUser_UserIDAndDateAndStatus(String userID, LocalDate date, int status);

    List<Appointment> findAppointmentsByUser_UserIDAndStatus(String userID, int status);
}
