package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    private final DentistRepository dentistRepository;

    private final ClinicRepository clinicRepository;

    public List<Appointment> findApointmentClinic(String staffmail) {
        try {
            Staff staffclient = staffRepository.findStaffByUserMail(staffmail);

            Clinic clinic = staffclient.getClinic();
            return appointmentRepository.findAppointmentByClinic(clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by clinic: " + e.getMessage(), e);
        }
    }

    public List<Appointment> customerAppointment(String cusid, String staffmail) {
        try {
            Staff staffclient = staffRepository.findStaffByUserMail(staffmail);
            Clinic clinic = staffclient.getClinic();
            return appointmentRepository.findAppointmentByUser_UserIDAndClinic(cusid, clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> customerAppointmentfollowdentist(String cusid, String dentist) {
        try {
            return appointmentRepository.getAppointmentByUser_UserIDAndDentist_User_MailAndStatusOrStatus(cusid, dentist, 1, 2);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }

    public Optional<List<Appointment>> findAppointmentByDentist(String mail) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailAndDateAndStatus(mail, LocalDate.now(), 1);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }

    public List<Appointment> findAllAppointmentByDentist(String mail) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailOrderByDateAsc(mail);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }


    public List<Appointment> findAppointmentHistory(Client user, LocalDate date, Integer status) {
        try {
            String userID = user.getUserID();

            if (date == null && status == null) {
                return appointmentRepository.findAppointmentByUser_UserID(userID);
            } else {
                if (status != null && date == null) {
                    return appointmentRepository.findAppointmentsByUser_UserIDAndStatus(userID, status);
                } else if (status == null && date != null) {
                    return appointmentRepository.findAppointmentByUser_UserIDAndDate(userID, date);
                } else {
                    return appointmentRepository.findAppointmentByUser_UserIDAndDateAndStatus(userID, date, status);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    public List<Appointment> getAppointmentsForWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        try {
            return appointmentRepository.findAppointmentsByDateBetweenAndStatusOrStatus(startOfWeek, endOfWeek, 1, 2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> searchAppointmentByStaff(LocalDate date, String name, String staffmail) {
        try {
            Staff staffclient = staffRepository.findStaffByUserMail(staffmail);

            Clinic clinic = staffclient.getClinic();
            return appointmentRepository.findByDateAndClinicAndUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(date, clinic, name, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> searchAppointmentByDentist(LocalDate date, String name, String staffmail) {
        try {
            Dentist dentist = dentistRepository.findDentistByUserMail(staffmail);

            Clinic clinic = dentist.getClinic();
            return appointmentRepository.findByDateAndClinicAndUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(date, clinic, name, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<List<Appointment>> searchAppointmentByCustomer(LocalDate date, String name, String mail) {
        try {
            Client client = userRepository.findUserByMail(mail);

            return appointmentRepository.searchAppointmentByDateAndUser_NameOrDependent_Name(date, name, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<List<Appointment>> findAllAppointmentByCustomer(String mail) {
        try {
            Client client = userRepository.findUserByMail(mail);

            return appointmentRepository.findAppointmentsByUser(client);
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

    public int totalAppointmentsInMonthByBoss() {
        return appointmentRepository.countAppointmentsByMonthPresentByBoss(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }

    public int totalAppointmentsInYearByBoss() {
        return appointmentRepository.countAppointmentsByYearPresentByBoss(LocalDate.now().getYear());
    }

    public int totalAppointmentsInMonthByStaff(Staff staff) {
        return appointmentRepository.countAppointmentsByMonthPresentByStaff(LocalDate.now().getMonthValue(), LocalDate.now().getYear(), staff);
    }

    public int totalAppointmentsInYearByStaff(Staff staff) {
        return appointmentRepository.countAppointmentsByYearPresentByStaff(LocalDate.now().getYear(), staff);
    }

    public Map<String, List<Appointment>> getDailyAppointmentsByDentist(LocalDate date, Staff staff) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateAndDentist_StaffAndStatusOrStatus(date, staff, 1, 2);
        Map<String, List<Appointment>> appointmentsByDentist = new HashMap<>();

        for (Appointment appointment : appointments) {
            String dentist = appointment.getDentist().getDentistID();
            appointmentsByDentist.computeIfAbsent(dentist, k -> new ArrayList<>()).add(appointment);
        }

        return appointmentsByDentist;
    }

    public Map<Integer, Long> getMonthlyAppointmentsByDentist(int year, int month, LocalDate startDate, LocalDate endDate, Staff staff) {
        Map<Integer, Long> monthlyAppointmentCounts = new HashMap<>();

        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndDentistStaffAndStatusOrStatus(startDate, endDate, staff, 1, 2)
                .orElse(Collections.emptyList());

        //số lượng cuộc hẹn cho từng nha sĩ
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
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateAndStatusOrStatus(date, 1, 2);
        Map<Clinic, List<Appointment>> appointmentsByClinic = new HashMap<>();

        for (Appointment appointment : appointments) {
            Clinic clinic = appointment.getClinic();
            appointmentsByClinic.computeIfAbsent(clinic, k -> new ArrayList<>()).add(appointment);
        }

        return appointmentsByClinic;
    }

    public Map<String, Map<Integer, Long>> getAppointmentsByClinicsForYear(int year) {
        Map<String, Map<Integer, Long>> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndStatusOrStatus(startDate, endDate, 1, 2);
            if (!appointments.isEmpty()) {
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

    public Map<String, Map<Integer, Long>> getClinicAppointmentsForYear(Client manager, int year) {
        Map<String, Map<Integer, Long>> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndStatusOrStatusAndClinicUser(startDate, endDate, 1, 2, manager);
            if (!appointments.isEmpty()) {
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

    public int totalAppointmentsInMonthByManager(Client manager) {

        return appointmentRepository.countAppointmentsByMonthPresentByManager(LocalDate.now().getMonthValue(), LocalDate.now().getYear(), manager);
    }

    public int totalAppointmentsInYearByManager(Client manager) {
        return appointmentRepository.countAppointmentsByYearPresentByManager(LocalDate.now().getYear(), manager);
    }
}
