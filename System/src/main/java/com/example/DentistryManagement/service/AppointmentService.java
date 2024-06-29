package com.example.DentistryManagement.service;

import com.example.DentistryManagement.DTO.ClinicDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    private final DentistRepository dentistRepository;

    private final ClinicRepository clinicRepository;
    private final UserMapping userMapping;

    public List<Appointment> findAppointmentInClinic(String staffmail) {
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
            return appointmentRepository.getAppointmentByUser_UserIDAndDentist_User_Mail(cusid, dentist);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }

    public List<Appointment> findAppointmentByDentist(String mail) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailAndDateAndStatus(mail, LocalDate.now(), 1);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }

    public List<Appointment> findAllAppointmentByDentist(String mail, Clinic clinic) {
        try {
            return appointmentRepository.getAppointmentByDentist_User_MailAndClinicOrderByDateAsc(mail, clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }


    public List<Appointment> findAppointmentHistory(Client user, LocalDate date, Integer status) {
        try {
            String userID = user.getUserID();
            List<Appointment> appointmentsHistory;
            if (date == null && status == null) {
                appointmentsHistory = appointmentRepository.findAppointmentByUser_UserID(userID);
            } else {
                if (status != null && date == null) {
                    appointmentsHistory = appointmentRepository.findAppointmentsByUser_UserIDAndStatus(userID, status);
                } else if (status == null && date != null) {
                    appointmentsHistory = appointmentRepository.findAppointmentByUser_UserIDAndDate(userID, date);
                } else {
                    appointmentsHistory = appointmentRepository.findAppointmentByUser_UserIDAndDateAndStatus(userID, date, status);
                }
            }
            return appointmentsHistory;
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

    public List<Appointment> getAppointmentsForWeek(LocalDate startOfWeek, LocalDate endOfWeek, Dentist dentist) {
        try {
            List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetween(startOfWeek, endOfWeek);
            List<Appointment> filterAppointments = new ArrayList<>();
            for (Appointment appointment : appointments) {
                if (appointment.getDentist() == dentist) {
                    if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                        filterAppointments.add(appointment);
                    }
                }
            }
            return filterAppointments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> searchAppointmentByStaff(LocalDate date, String name, String staffmail) {
        try {
            Staff staffClient = staffRepository.findStaffByUserMail(staffmail);
            List<Appointment> appointments = appointmentRepository.findByDateOrUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(date, name, name);
            List<Appointment> filterAppointments = new ArrayList<>();
            for (Appointment appointment : appointments) {
                if (appointment.getClinic().getClinicID().equals(staffClient.getClinic().getClinicID())) {
                    filterAppointments.add(appointment);
                }
            }
            return filterAppointments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> searchAppointmentByDentist(LocalDate date, String name, Dentist dentist) {
        try {
            List<Appointment> appointments = appointmentRepository.findByDateOrUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(date, name, name);
            List<Appointment> filterAppointments = new ArrayList<>();
            for (Appointment appointment : appointments) {
                if (appointment.getDentist() == dentist) {
                    filterAppointments.add(appointment);
                }
            }
            return filterAppointments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> searchAppointmentByCustomer(LocalDate date, String name, String mail) {
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

    public Map<String, Integer> getDailyAppointmentsByDentist(LocalDate date, Staff staff) {
        List<Appointment> appointmentsBase = appointmentRepository.findAppointmentsByDateAndDentist_Staff(date, staff);
        Map<String, Integer> appointmentsByDentist = new HashMap<>();

        for (Appointment appointment : appointmentsBase) {
            if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                Client dentist = appointment.getDentist().getUser();
                UserDTO dentistDTO = new UserDTO().getUserDTOFromUser(dentist);
                appointmentsByDentist.put("ID: " + dentistDTO.getId() + ",Name: " + dentistDTO.getName(), appointmentsByDentist.getOrDefault("ID: " + dentistDTO.getId() + ",Name: " + dentistDTO.getName(), 0) + 1);
            }
        }

        return appointmentsByDentist;
    }

    public Map<Integer, Long> getMonthlyAppointmentsByDentist(LocalDate startDate, LocalDate endDate, Staff staff) {
        Map<Integer, Long> monthlyAppointmentCounts = new HashMap<>();
        List<Appointment> appointmentsBase = appointmentRepository.findAppointmentsByDateBetweenAndDentistStaff(startDate, endDate, staff);
        List<Appointment> appointments = Optional.ofNullable(appointmentsBase)
                .orElse(new ArrayList<>())
                .stream()
                .filter(appointment -> appointment.getStatus() == 1 || appointment.getStatus() == 2)
                .collect(Collectors.toList());
        //số lượng cuộc hẹn cho từng nha sĩ
        appointments.forEach(appointment -> {
            LocalDate appointmentDate = appointment.getDate();
            int dayOfMonth = appointmentDate.getDayOfMonth();
            long count = monthlyAppointmentCounts.getOrDefault(dayOfMonth, 0L);
            monthlyAppointmentCounts.put(dayOfMonth, count + 1);
        });

        return monthlyAppointmentCounts;
    }

    // Lấy số lượng cuộc hẹn của cac dentist thuoc staff trong cả năm
    public Map<Integer, Long> getAppointmentsByStaffForYear(Staff staff, int year) {
        Map<Integer, Long> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            LocalDate startDate = LocalDate.of(year, currentMonth, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            Map<Integer, Long> monthlyCounts = getMonthlyAppointmentsByDentist(startDate, endDate, staff);

            // Tổng hợp số lượng cuộc hẹn trong tháng vào tổng số lượng của năm
            monthlyCounts.forEach((key, value) -> yearlyAppointmentCounts.merge(currentMonth, value, Long::sum));
        }

        return yearlyAppointmentCounts;
    }

    public Map<String, List<Appointment>> getDailyAppointmentsByClinic(LocalDate date) {
        List<Appointment> appointmentBase = appointmentRepository.findAppointmentsByDate(date);
        List<Appointment> appointments = new ArrayList<>();
        for (Appointment appointment : appointmentBase) {
            if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                appointments.add(appointment);
            }
        }
        Map<String, List<Appointment>> appointmentsByClinic = new HashMap<>();

        for (Appointment appointment : appointments) {
            Clinic clinic = appointment.getClinic();
            ClinicDTO clinicDTO = new ClinicDTO().clinicMapping(clinic);
            appointmentsByClinic.computeIfAbsent("ID" + clinicDTO.getId() + ",Name" + clinicDTO.getName(), k -> new ArrayList<>()).add(appointment);
        }

        return appointmentsByClinic;
    }

    public Map<String, Map<Integer, Long>> getAppointmentsByClinicsForYear(int year) {
        Map<String, Map<Integer, Long>> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<Appointment> appointmentBase = appointmentRepository.findAppointmentsByDateBetween(startDate, endDate);
            List<Appointment> appointments = new ArrayList<>();
            for (Appointment appointment : appointmentBase) {
                if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                    appointments.add(appointment);
                }
            }
            if (!appointments.isEmpty()) {
                for (Appointment appointment : appointments) {
                    Clinic clinic = appointment.getClinic();
                    String clinicKey = clinic.getClinicID() + " " + clinic.getName();
                    yearlyAppointmentCounts.putIfAbsent(clinicKey, new HashMap<>());
                    Map<Integer, Long> monthlyCounts = yearlyAppointmentCounts.get(clinicKey);
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
            List<Appointment> appointmentBase = appointmentRepository.findAppointmentsByDateBetweenAndClinicUser(startDate, endDate, manager);
            List<Appointment> appointments = new ArrayList<>();
            for (Appointment appointment : appointmentBase) {
                if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                    appointments.add(appointment);
                }
            }
            if (!appointments.isEmpty()) {
                for (Appointment appointment : appointments) {
                    Clinic clinic = appointment.getClinic();
                    String clinicName = clinic.getName() + " " + clinic.getAddress();
                    yearlyAppointmentCounts.putIfAbsent(clinicName, new HashMap<>());
                    Map<Integer, Long> monthlyCounts = yearlyAppointmentCounts.get(clinicName);
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
