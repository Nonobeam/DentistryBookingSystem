
package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findAppointmentByUser_UserIDAndClinic(String customerID, Clinic clinic);

    List<Appointment> getAppointmentByDentist_User_MailAndClinicOrderByDateAsc(String dentistMail, Clinic clinic);

    List<Appointment> findAppointmentByClinic(Clinic c);

    List<Appointment> getAppointmentByDentist_User_MailAndDateAndStatus(String dentistMail, LocalDate date, int status);

    Optional<List<Appointment>> getAppointmentByUser_UserIDAndDentist_User_Mail(String customerId, String dentistMail);

    List<Appointment> findAppointmentsByDateAndStatus(LocalDate date, int status);

    Appointment findAppointmentByAppointmentID(String appointmentID);

    Optional<List<Appointment>> findAppointmentsByUserAndStatus(Client client, int status);

    List<Appointment> findAppointmentsByDateBetweenAndClinicUser(LocalDate startOfWeek, LocalDate endOfWeek, Client manager);

    List<Appointment> searchAppointmentByDateAndUser_NameOrDependent_Name(LocalDate date, String name, String dependentName);

    List<Appointment> findAppointmentsByDateAndDentist_Staff(LocalDate date, Staff staff);

    List<Appointment> findAppointmentsByDateBetweenAndDentistStaff(LocalDate startDate, LocalDate endDate, Staff staff);

    List<Appointment> findByDateOrUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(LocalDate date, String name1, String dependentName);

    Optional<List<Appointment>> findAppointmentsByUser(Client client);

    @Query("SELECT COUNT(*) FROM Appointment WHERE MONTH(date) = :month AND YEAR(date) = :year")
    int countAppointmentsByMonthPresentByBoss(int month, int year);

    @Query("SELECT COUNT(*) FROM Appointment WHERE YEAR(date) = :year")
    int countAppointmentsByYearPresentByBoss(int year);

    @Query("SELECT COUNT(*) FROM Appointment WHERE MONTH(date) = :month AND YEAR(date) = :year and dentist.staff =:staff")
    int countAppointmentsByMonthPresentByStaff(int month, int year, Staff staff);

    @Query("SELECT COUNT(*) FROM Appointment WHERE YEAR(date) = :year and dentist.staff =:staff")
    int countAppointmentsByYearPresentByStaff(int year, Staff staff);


    @Query("SELECT COUNT(*) FROM Appointment WHERE MONTH(date) = :month AND YEAR(date) = :year and clinic.user=:manager")
    int countAppointmentsByMonthPresentByManager(int month, int year, Client manager);

    @Query("SELECT COUNT(*) FROM Appointment WHERE YEAR(date) = :year and clinic.user=:manager")
    int countAppointmentsByYearPresentByManager(int year, Client manager);

    List<Appointment> findAppointmentByUser_UserID(String customerID);

    List<Appointment> findAppointmentByUser_UserIDAndDate(String userID, LocalDate date);

    List<Appointment> findAppointmentByUser_UserIDAndDateAndStatus(String userID, LocalDate date, int status);

    List<Appointment> findAppointmentsByUser_UserIDAndStatus(String userID, int status);

    List<Appointment> findAppointmentsByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Appointment> findAppointmentsByDate(LocalDate date);
}
