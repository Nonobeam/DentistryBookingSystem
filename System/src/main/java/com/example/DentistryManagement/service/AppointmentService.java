package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.repository.ClinicRepository;
import com.example.DentistryManagement.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private  final StaffRepository userRepository;
    private final ClinicRepository clinicRepository;

    public Optional<List<Appointment>> findApointmentclinic(String clinicID) {
        try {
            Clinic clinic = clinicRepository.findById(clinicID).orElse(null);
            return appointmentRepository.findAppointmentByClinic(clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> customerAppointment(String cusid, String staffmail) {
        try {
            Staff staffclient = userRepository.findStaffByUserMail(staffmail);
            Clinic clinic = staffclient.getClinic();
            return appointmentRepository.findAppointmentByUser_UserIDAndClinic(cusid, clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }
    public Optional<List<Appointment>> customerAppointfollowdentist(String cusid, String dentist) {
        try {
            return appointmentRepository.getAppointmentByUser_UserIDAndDentist_User_MailAndStatusOrStatus(cusid, dentist,1,2);
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

    public Optional<List<Appointment>> getAppointmentsForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        try {
            return Optional.ofNullable(appointmentRepository.findAppointmentsByDateBetweenAndStatusOrStatus(startOfWeek, endOfWeek, 1, 2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<List<Appointment>> searchAppointmentByWorker(LocalDate date,String name) {
        try {
            return appointmentRepository.searchAppointmentByDateAndUser_FirstNameOrUser_LastNameOrDependent_FirstNameOrDependent_LastName(date,name,name,name,name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appointment updateAppointment(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, List<Appointment>> getDailyAppointmentsByDentist(LocalDate date,Staff staff) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateAndDentist_StaffAndStatusOrStatus(date, staff,1, 2);
        Map<String, List<Appointment>> appointmentsByDentist = new HashMap<>();

        for (Appointment appointment : appointments) {
            String dentist = appointment.getDentist().getDentistID();
            appointmentsByDentist.computeIfAbsent(dentist, k -> new ArrayList<>()).add(appointment);
        }

        return appointmentsByDentist;
    }

    public Map<Integer, Long> getMonthlyAppointmentsByDentist(int year, int month, LocalDate startDate, LocalDate endDate, Staff staff) {
        Map<Integer, Long> monthlyAppointmentCounts = new HashMap<>();

        // Lấy danh sách cuộc hẹn theo từng ngày trong tháng
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndDentistStaffAndStatusOrStatus(startDate, endDate, staff,1,2)
                .orElse(Collections.emptyList());

        // Tính số lượng cuộc hẹn cho từng nha sĩ
        appointments.forEach(appointment -> {
            LocalDate appointmentDate = appointment.getDate();
            int dayOfMonth = appointmentDate.getDayOfMonth();
            long count = monthlyAppointmentCounts.getOrDefault(dayOfMonth, 0L);
            monthlyAppointmentCounts.put(dayOfMonth, count + 1);
        });

        return monthlyAppointmentCounts;
    }

    // Lấy số lượng cuộc hẹn của từng nha sĩ trong cả năm
    public Map<Integer, Long> getAppointmentsByStaffForYear(Staff staff, int year) {
        Map<Integer, Long> yearlyAppointmentCounts = new HashMap<>();
        List<Dentist> dentists = staff.getDentistList();

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            LocalDate startDate = LocalDate.of(year, currentMonth, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            Map<Integer, Long> monthlyCounts = getMonthlyAppointmentsByDentist(year, currentMonth, startDate, endDate, staff);

            // Tổng hợp số lượng cuộc hẹn trong tháng vào tổng số lượng của năm
            monthlyCounts.forEach((key, value) -> yearlyAppointmentCounts.merge(currentMonth, value, Long::sum));
        }

        return yearlyAppointmentCounts;
    }
    public Map<Clinic, List<Appointment>> getDailyAppointmentsByClinic(LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateAndStatusOrStatus(date,1, 2);
        Map<Clinic, List<Appointment>> appointmentsByClinic = new HashMap<>();

        for (Appointment appointment : appointments) {
            Clinic clinic = appointment.getClinic();
            appointmentsByClinic.computeIfAbsent(clinic, k -> new ArrayList<>()).add(appointment);
        }

        return appointmentsByClinic;
    }

    // Method to get yearly appointments by clinics
    public Map<String, Map<Integer, Long>> getAppointmentsByClinicsForYear(int year) {
        Map<String, Map<Integer, Long>> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndStatusOrStatus(startDate, endDate, 1, 2);
            if(!appointments.isEmpty()) {
                for (Appointment appointment : appointments) {
                    Clinic clinic = appointment.getClinic();
                    String clinicid = clinic.getClinicID();
                    yearlyAppointmentCounts.putIfAbsent(clinicid, new HashMap<>());
                    Map<Integer, Long> monthlyCounts = yearlyAppointmentCounts.get(clinicid);
                    monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0L) + 1);
                }
            }

        }

        return yearlyAppointmentCounts;
    }
}
