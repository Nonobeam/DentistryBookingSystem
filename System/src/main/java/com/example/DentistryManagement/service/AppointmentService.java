package com.example.DentistryManagement.service;

import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.DTO.ClinicDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final DentistScheduleService dentistScheduleService;
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final UserMapping userMapping;

    public List<Appointment> findAppointmentInClinic(String staffMail) {
        try {
            Staff staff = staffRepository.findStaffByUserMail(staffMail);

            Clinic clinic = staff.getClinic();
            return appointmentRepository.findAppointmentByClinic(clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by clinic: " + e.getMessage(), e);
        }
    }

    public List<Appointment> customerAppointment(String customerId, String staffMail) {
        try {
            Staff staff = staffRepository.findStaffByUserMail(staffMail);
            Clinic clinic = staff.getClinic();
            return appointmentRepository.findAppointmentByUser_UserIDAndClinic(customerId, clinic);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching appointment list by customer ID and clinic: " + e.getMessage(), e);
        }
    }

    public List<Appointment> customerAppointmentFollowDentist(String customerId, String dentist) {
        try {
            return appointmentRepository.getAppointmentByUser_UserIDAndDentist_User_Mail(customerId, dentist);
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
                } else if (status == null) {
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


    public Optional<List<Appointment>> findAppointmentsByUserAndStatus(Client userId, int status) {
        try {
            return appointmentRepository.findAppointmentsByUserAndStatus(userId, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> searchAppointmentByStaff(LocalDate date, String name, String staffMail) {
        try {
            Staff staffClient = staffRepository.findStaffByUserMail(staffMail);
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
                UserDTO dentistDTO = userMapping.getUserDTOFromUser(dentist);
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
                .toList();
        appointments.forEach(appointment -> {
            LocalDate appointmentDate = appointment.getDate();
            int dayOfMonth = appointmentDate.getDayOfMonth();
            long count = monthlyAppointmentCounts.getOrDefault(dayOfMonth, 0L);
            monthlyAppointmentCounts.put(dayOfMonth, count + 1);
        });

        return monthlyAppointmentCounts;
    }

    public Map<Integer, Long> getAppointmentsByStaffForYear(Staff staff, int year) {
        Map<Integer, Long> yearlyAppointmentCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            LocalDate startDate = LocalDate.of(year, currentMonth, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            Map<Integer, Long> monthlyCounts = getMonthlyAppointmentsByDentist(startDate, endDate, staff);

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
            appointmentsByClinic.computeIfAbsent("Name " + clinicDTO.getName() + " Address " + clinicDTO.getAddress(), k -> new ArrayList<>()).add(appointment);
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

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAppointmentsByDateAndStatus(LocalDate workDate, int status) {
        return appointmentRepository.findAppointmentsByDateAndStatus(workDate, status);
    }

    public List<Appointment> findAppointmentsByDateBetween(LocalDate startDate, LocalDate endDate, Staff staff) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndDentistStaff(startDate, endDate, staff);
        appointments.removeIf(appointment -> appointment.getStatus() == 0);
        return appointments;
    }

    public List<Appointment> findAppointmentsByDateBetweenDentist(LocalDate startDate, LocalDate endDate, Dentist dentist) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDateBetweenAndDentist(startDate, endDate, dentist);
        appointments.removeIf(appointment -> appointment.getStatus() == 0);
        return appointments;
    }

    public List<AppointmentDTO> appointmentDTOList(List<Appointment> appointmentList) {
        List<AppointmentDTO> appointmentDTOList;
        appointmentDTOList = appointmentList.stream()
                .map(appointmentEntity -> {
                    AppointmentDTO appointment = new AppointmentDTO();
                    appointment.setAppointmentId(appointmentEntity.getAppointmentID());
                    appointment.setServices(appointmentEntity.getServices().getName());
                    appointment.setStatus(appointmentEntity.getStatus());
                    appointment.setDate(appointmentEntity.getDate());
                    appointment.setDentist(appointmentEntity.getDentist().getUser().getName());
                    appointment.setTimeSlot(appointmentEntity.getTimeSlot().getStartTime());
                    if (appointmentEntity.getStaff() != null) {
                        if (appointmentEntity.getUser() != null) {
                            appointment.setUser(appointmentEntity.getUser().getName());
                        } else {
                            appointment.setDependent(appointmentEntity.getDependent().getName());
                        }
                        appointment.setStaff(appointmentEntity.getStaff().getUser().getName());
                    } else {
                        if (appointmentEntity.getDependent() != null) {
                            appointment.setDependent(appointmentEntity.getDependent().getName());
                        } else
                            appointment.setUser(appointmentEntity.getUser().getName());
                    }

                    return appointment;
                })
                .toList();
        return appointmentDTOList;
    }


    /**
     * @param staff Input Client staff
     * @param customer Input Client customer
     * @param dentistSchedule Input DentistSchedule
     * @param services Input Services
     * @param dependent Input Dependent dependent
     * @return appointment
     */
    public Appointment createAppointment(@Nullable Client staff, Client customer , DentistSchedule dentistSchedule, Services services, Dependent dependent) {
        Appointment.AppointmentBuilder appointmentBuilder = Appointment.builder()
                .staff(staff.getStaff())
                .user(customer)
                .clinic(dentistSchedule.getClinic())
                .date(dentistSchedule.getWorkDate())
                .timeSlot(dentistSchedule.getTimeslot())
                .dentist(dentistSchedule.getDentist())
                .services(services)
                .dentistScheduleId(dentistSchedule.getScheduleID())
                .status(1);

        if (dependent != null) {
            appointmentBuilder.dependent(dependent);
        }
        appointmentBuilder.build();

        dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
        Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
        otherSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(0)));

        return appointmentRepository.save(appointmentBuilder.build());
    }

    public LocalDate startUpdateTimeSlotDate(String clinicID) {
        LocalDate result;
        Appointment appointment = appointmentRepository.findTopByClinicOrderByDateDescStartTimeDesc(clinicID, PageRequest.of(0, 1)).get(0);
        result = appointment.getDate();
        if (result != null) {
            return result;
        }
        return null;
    }

}
