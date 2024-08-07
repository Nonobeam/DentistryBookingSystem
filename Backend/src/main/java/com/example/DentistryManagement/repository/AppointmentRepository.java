
package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import jnr.constants.platform.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findAppointmentByUser_UserIDAndClinic(String customerID, Clinic clinic);

    List<Appointment> getAppointmentByDentist_User_MailAndClinicOrderByDateAsc(String dentistMail, Clinic clinic);

    List<Appointment> findAppointmentByClinic(Clinic c);

    List<Appointment> getAppointmentByDentist_User_MailAndDateAndStatus(String dentistMail, LocalDate date, int status);

    List<Appointment> getAppointmentByUser_UserIDAndDentist_User_Mail(String customerId, String dentistMail);

    List<Appointment> findAppointmentsByDateAndStatus(LocalDate date, int status);

    Appointment findAppointmentByAppointmentID(String appointmentID);

    Optional<List<Appointment>> findAppointmentsByUserAndStatus(Client client, int status);

    List<Appointment> findAppointmentsByDateBetweenAndClinicUser(LocalDate startOfWeek, LocalDate endOfWeek, Client manager);

    List<Appointment> findAppointmentsByDateAndDentist_Staff(LocalDate date, Staff staff);

    List<Appointment> findAppointmentsByDateBetweenAndDentistStaff(LocalDate startDate, LocalDate endDate, Staff staff);

    List<Appointment> findByUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(String name1, String dependentName);

    @Query("SELECT COUNT(*) FROM Appointment WHERE MONTH(date) = :month AND YEAR(date) = :year and (status = 1 or status =2)")
    int countAppointmentsByMonthPresentByBoss(int month, int year);

    @Query("SELECT COUNT(*) FROM Appointment WHERE YEAR(date) = :year and (status = 1 or status =2)")
    int countAppointmentsByYearPresentByBoss(int year);

    @Query("SELECT COUNT(*) FROM Appointment WHERE MONTH(date) = :month AND YEAR(date) = :year and dentist.staff =:staff and (status = 1 or status =2)")
    int countAppointmentsByMonthPresentByStaff(int month, int year, Staff staff);

    @Query("SELECT COUNT(*) FROM Appointment WHERE YEAR(date) = :year and dentist.staff =:staff and (status = 1 or status =2)")
    int countAppointmentsByYearPresentByStaff(int year, Staff staff);


    @Query("SELECT COUNT(*) FROM Appointment WHERE MONTH(date) = :month AND YEAR(date) = :year and clinic.user=:manager and (status = 1 or status =2)")
    int countAppointmentsByMonthPresentByManager(int month, int year, Client manager);

    @Query("SELECT COUNT(*) FROM Appointment WHERE YEAR(date) = :year and clinic.user=:manager and (status = 1 or status =2)")
    int countAppointmentsByYearPresentByManager(int year, Client manager);

    List<Appointment> findAppointmentByUser_UserID(String customerID);

    List<Appointment> findAppointmentByUser_UserIDAndDate(String userID, LocalDate date);

    List<Appointment> findAppointmentByUser_UserIDAndDateAndStatus(String userID, LocalDate date, int status);

    List<Appointment> findAppointmentsByUser_UserIDAndStatus(String userID, int status);

    List<Appointment> findAppointmentsByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Appointment> findAppointmentsByDate(LocalDate date);

    List<Appointment> findAppointmentsByDateBetweenAndDentist(LocalDate startDate, LocalDate endDate, Dentist dentist);

    @Query("SELECT a FROM Appointment a WHERE a.clinic.clinicID = :clinicID ORDER BY a.date DESC")
    List<Appointment> findTopByClinicOrderByDateDescStartTimeDesc(@Param("clinicID") String clinicID, Pageable pageable);

    boolean existsByTimeSlotAndDateAndUser(TimeSlot timeSlot, LocalDate bookDate, Client user);
    List<Appointment> findAppointmentsByUser(Client client);

    List<Appointment> findAppointmentsByDentistAndStatusAndStarAppointmentGreaterThan(Dentist dentist, int status,int star);

    List<Appointment> findAppointmentByDentistAndStatus(Dentist dentist, int status);

    boolean existsByDependentAndUserAndTimeSlotAndStatus(Dependent dependent, Client user, TimeSlot timeSlot, int status);
}
