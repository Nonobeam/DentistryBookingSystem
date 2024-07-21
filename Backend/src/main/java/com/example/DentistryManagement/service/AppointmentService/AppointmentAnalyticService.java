package com.example.DentistryManagement.service.AppointmentService;

import com.example.DentistryManagement.DTO.ClinicDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentAnalyticService {
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final UserMapping userMapping;

    public List<Appointment> getAppointmentsInAClinicByStaffMail(String staffMail) {
        try {
            List<Appointment> appointmentsInAClinic;
            Staff staff = staffRepository.findStaffByUserMail(staffMail);
            Clinic clinic = staff.getClinic();
            appointmentsInAClinic = appointmentRepository.findAppointmentByClinic(clinic);

            return appointmentsInAClinic;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Appointment> customerAppointmentFollowDentist(String customerId, String dentist) {
        try {
            List<Appointment> customerAppointments;
            customerAppointments = appointmentRepository.getAppointmentByUser_UserIDAndDentist_User_Mail(customerId, dentist);

            return customerAppointments;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }


    public List<Appointment> getAppointmentsByDentistMail(String mail) {
        try {
            List<Appointment> appointmentsByADentist;
            appointmentsByADentist = appointmentRepository.getAppointmentByDentist_User_MailAndDateAndStatus(mail, LocalDate.now(), 1);

            return appointmentsByADentist;
        } catch (Exception e) {
            throw e;
        }
    }


    public List<Appointment> getCustomerAppointmentsInAClinicByStaffMailAndCustomerId(String customerId, String staffMail) {
        try {
            List<Appointment> customerAppointmentsInAClinic;
            Staff staff = staffRepository.findStaffByUserMail(staffMail);
            Clinic clinic = staff.getClinic();
            customerAppointmentsInAClinic = appointmentRepository.findAppointmentByUser_UserIDAndClinic(customerId, clinic);

            return customerAppointmentsInAClinic;
        } catch (Exception e) {
            throw e;
        }
    }


    public List<Appointment> getAppointmentsInAClinicByCustomerMail(String mail, Clinic clinic) {
        try {
            List<Appointment> appointmentsInAClinic;
            appointmentsInAClinic = appointmentRepository.getAppointmentByDentist_User_MailAndClinicOrderByDateAsc(mail, clinic);
            return appointmentsInAClinic;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }


    public List<Appointment> getAppointmentsByUserAndByDateOrStatus(Client user, LocalDate date, Integer status) {
        try {
            String userID = user.getUserID();
            List<Appointment> appointmentsHistory;
            if (date == null && status == null) {
                appointmentsHistory = appointmentRepository.findAppointmentByUser_UserID(userID);
            } else {
                if (status != null && date == null) {
                    appointmentsHistory = appointmentRepository.findAppointmentsByUser_UserIDAndStatus(userID, status);
                } else if (status == null) {
                    appointmentsHistory = appointmentRepository.findAppointmentByUser_UserIDAndDate(userID, date);
                } else {
                    appointmentsHistory = appointmentRepository.findAppointmentByUser_UserIDAndDateAndStatus(userID, date, status);
                }
            }
            return appointmentsHistory.stream().sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment -> Appointment.getTimeSlot().getStartTime())).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Appointment getAppointmentById(String appointmentID) {
        try {
            return appointmentRepository.findAppointmentByAppointmentID(appointmentID);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by dentist ID: " + e.getMessage(), e);
        }
    }


    public Optional<List<Appointment>> getAppointmentsByUserAndStatus(Client userId, int status) {
        try {
            return appointmentRepository.findAppointmentsByUserAndStatus(userId, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Appointment> getAppointmentsByCustomerNameOrDependentNameAndStaffMail(String name, String staffMail) {
        try {
            Staff staffClient = staffRepository.findStaffByUserMail(staffMail);
            List<Appointment> appointments = appointmentRepository.findByUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(name, name);
            List<Appointment> filterAppointments = new ArrayList<>();
            for (Appointment appointment : appointments) {
                String appointmentID = appointment.getAppointmentID();
                String staffClinicID = staffClient.getClinic().getClinicID();
                if (appointmentID.equals(staffClinicID)) {
                    filterAppointments.add(appointment);
                }
            }
            return filterAppointments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> getAppointmentsByCustomerNameOrDependentNameAndDentist(String name, Dentist dentist) {
        try {
            List<Appointment> appointments = appointmentRepository.findByUserNameContainingIgnoreCaseOrDependentNameContainingIgnoreCase(name, name);
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


    public Map<String, Integer> getAppointmentsByDateAndDentist(LocalDate date, Staff staff) {
        List<Appointment> appointmentsBase = appointmentRepository.findAppointmentsByDateAndDentist_Staff(date, staff);
        Map<String, Integer> appointmentsByDentist = new HashMap<>();

        for (Appointment appointment : appointmentsBase) {
            if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                Client dentist = appointment.getDentist().getUser();
                UserDTO dentistDTO = userMapping.getUserDTOFromUser(dentist);
                appointmentsByDentist.put("Name: " + dentistDTO.getName(), appointmentsByDentist.getOrDefault("Name: " + dentistDTO.getName(), 0) + 1);
            }
        }

        return appointmentsByDentist;
    }

    public Map<Integer, Long> getAppointmentsByDateAndDentist(LocalDate startDate, LocalDate endDate, Staff staff) {
        Map<Integer, Long> monthlyAppointmentCounts = new HashMap<>();
        List<Appointment> appointmentsBase = appointmentRepository.findAppointmentsByDateBetweenAndDentistStaff(startDate, endDate, staff);
        List<Appointment> appointments = Optional.ofNullable(appointmentsBase)
                .orElse(new ArrayList<>())
                .stream()
                .filter(appointment -> appointment.getStatus() == 1 || appointment.getStatus() == 2)
                .toList();
        appointments.forEach(appointment -> {
            LocalDate appointmentDate = appointment.getDate();
            int dayOfMonth = appointmentDate.getDayOfMonth();
            long count = monthlyAppointmentCounts.getOrDefault(dayOfMonth, 0L);
            monthlyAppointmentCounts.put(dayOfMonth, count + 1);
        });

        return monthlyAppointmentCounts;
    }

    public Map<Integer, Long> getAppointmentsByYearAndStaff(Staff staff, int year) {
        Map<Integer, Long> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            LocalDate startDate = LocalDate.of(year, currentMonth, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            Map<Integer, Long> monthlyCounts = getAppointmentsByDateAndDentist(startDate, endDate, staff);

            monthlyCounts.forEach((key, value) -> yearlyAppointmentCounts.merge(currentMonth, value, Long::sum));
        }

        return yearlyAppointmentCounts;
    }

    public Map<String, Integer> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointmentBase = appointmentRepository.findAppointmentsByDate(date);
        List<Appointment> appointments = new ArrayList<>();

        for (Appointment appointment : appointmentBase) {
            if (appointment.getStatus() == 1 || appointment.getStatus() == 2) {
                appointments.add(appointment);
            }
        }

        Map<String, Integer> appointmentsByClinic = new HashMap<>();

        for (Appointment appointment : appointments) {
            Clinic clinic = appointment.getClinic();
            ClinicDTO clinicDTO = new ClinicDTO().clinicMapping(clinic);
            String clinicInfo = "Name " + clinicDTO.getName() + "- Address " + clinicDTO.getAddress();

            appointmentsByClinic.put(clinicInfo, appointmentsByClinic.getOrDefault(clinicInfo, 0) + 1);
        }

        return appointmentsByClinic;
    }


    public Map<String, Map<Integer, Long>> getAppointmentsByYear(int year) {
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
                    String clinicKey = clinic.getName() + "-" + clinic.getAddress();
                    yearlyAppointmentCounts.putIfAbsent(clinicKey, new HashMap<>());
                    Map<Integer, Long> monthlyCounts = yearlyAppointmentCounts.get(clinicKey);
                    monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0L) + 1);
                }
            }

        }

        return yearlyAppointmentCounts;
    }

    public Map<String, Map<Integer, Long>> getAppointmentsByYearAndManager(Client manager, int year) {
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
                    String clinicName = clinic.getName() + "-" + clinic.getAddress();
                    yearlyAppointmentCounts.putIfAbsent(clinicName, new HashMap<>());
                    Map<Integer, Long> monthlyCounts = yearlyAppointmentCounts.get(clinicName);
                    monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0L) + 1);
                }
            }

        }

        return yearlyAppointmentCounts;
    }

    public int totalAppointmentsInMonth() {
        return appointmentRepository.countAppointmentsByMonthPresentByBoss(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }

    public int totalAppointmentsInYear() {
        return appointmentRepository.countAppointmentsByYearPresentByBoss(LocalDate.now().getYear());
    }

    public int totalAppointmentsInMonthByStaff(Staff staff) {
        return appointmentRepository.countAppointmentsByMonthPresentByStaff(LocalDate.now().getMonthValue(), LocalDate.now().getYear(), staff);
    }

    public int totalAppointmentsInYearByStaff(Staff staff) {
        return appointmentRepository.countAppointmentsByYearPresentByStaff(LocalDate.now().getYear(), staff);
    }

    public int totalAppointmentsInMonthByManager(Client manager) {

        return appointmentRepository.countAppointmentsByMonthPresentByManager(LocalDate.now().getMonthValue(), LocalDate.now().getYear(), manager);
    }

    public int totalAppointmentsInYearByManager(Client manager) {
        return appointmentRepository.countAppointmentsByYearPresentByManager(LocalDate.now().getYear(), manager);
    }

    public List<Appointment> findAppointmentsByDateAndStatus(LocalDate workDate, int status) {
        return appointmentRepository.findAppointmentsByDateAndStatus(workDate, status);
    }

    public List<Appointment> findAppointmentsByDateAndStaff(LocalDate startDate, LocalDate endDate, Staff staff) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndDentistStaff(startDate, endDate, staff);
        appointments.removeIf(appointment -> appointment.getStatus() == 0);
        return appointments;
    }

    public List<Appointment> findAppointmentsByDateAndDentist(LocalDate startDate, LocalDate endDate, Dentist dentist) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndDentist(startDate, endDate, dentist);
        appointments.removeIf(appointment -> appointment.getStatus() == 0);
        return appointments;
    }

    public List<Appointment> getAppointmentByUnFeedback(Client user) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByUser(user);
        List<Appointment> response = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == 2) {
                if (appointment.getStarAppointment() == 0) {
                    response.add(appointment);
                }
            }
        }
        return response;
    }

    public double totalStarByDentist(Client client) {
        List<Appointment> totalAppointment = appointmentRepository.findAppointmentsByDentistAndStatusAndStarAppointmentGreaterThan(client.getDentist(), 2, 0);
        double total = totalAppointment.size();
        double stars = 0;
        for (Appointment appointment : totalAppointment) {
            stars += appointment.getStarAppointment();
        }
        return Math.round(stars / total * 10.0) / 10.0;
    }

    public Map<String, Double> getRatingDentistByStaff(Staff staff) {
        Map<String, Double> dentistRatings = new HashMap<>();

        List<Dentist> managedDentists = staff.getDentistList();

        for (Dentist dentist : managedDentists) {
            Client clientForDentist = new Client();
            clientForDentist.setDentist(dentist);

            double rating = totalStarByDentist(clientForDentist);
            dentistRatings.put(dentist.getUser().getName(), rating);
        }

        return dentistRatings;
    }

    public Map<String, Double> getRatingDentistByManager(Client manager) {
        Map<String, Double> dentistRatings = new HashMap<>();

        List<Dentist> managedDentists = manager.getClinicList().stream().map(clinic -> (Dentist) clinic.getDentistList()).toList();

        for (Dentist dentist : managedDentists) {
            Client clientForDentist = new Client();
            clientForDentist.setDentist(dentist);

            double rating = totalStarByDentist(clientForDentist);
            dentistRatings.put(dentist.getUser().getName(), rating);
        }

        return dentistRatings;
    }

    public List<Appointment> getAppointmentByDentistAndStatus(Dentist dentist, int status) {
        return appointmentRepository.findAppointmentByDentistAndStatus(dentist, status);
    }
}
