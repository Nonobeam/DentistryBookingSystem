package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.repository.ClinicRepository;
import com.example.DentistryManagement.repository.StaffRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
private  final StaffRepository userRepository;
private final ClinicRepository clinicRepository;
    public Optional<List<Appointment>> findApointmentclinic(String staffmail) {
        try {
            Staff staffclient = userRepository.findStaffByUserMail(staffmail);

            Clinic clinic = staffclient.getClinic();
            return appointmentRepository.findAppointmentByClinic(clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> customerAppointment(String cusid, String staffmail) {
        try {
            return appointmentRepository.findAppointmentByClinicStaff(cusid, staffmail);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }
    public Optional<List<Appointment>> customerAppointfollowdentist(String cusid, String dentist) {
        try {
            return appointmentRepository.getAppointmentByCustomerIdAndDentist_User_MailAndStatus(cusid, dentist,1);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> dentistAppointment(String mail) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailOrderByDateAsc(mail);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> findAppointByDentist(String mail) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailAndDateAndStatus(mail, LocalDate.now(),1);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> findAllAppointByDentist(String mail) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailOrderByDateAsc(mail);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }


//    public Optional<List<Appointment>> findAppointmentsByUserAndDateAndStatus(Client user, LocalDate date, int status) {
//        try {
//            return appointmentRepository.findAppointmentsByUserAndDateAndStatus(user, date, status);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    public Appointment AppointmentUpdate(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }

    public Appointment findAppointmentById(String appointmentID) {
        try {
            return appointmentRepository.findAppointmentByAppointmentID(appointmentID);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> findAppointmentsByDateAndStatus(LocalDate date, int status) {
        try {
            return appointmentRepository.findAppointmentsByDateAndStatus(date, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<List<Appointment>> findAppointmentsByUserAndStatus(Client userId, int status) {
        try {
            return appointmentRepository.findAppointmentsByUserAndStatus(userId, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
