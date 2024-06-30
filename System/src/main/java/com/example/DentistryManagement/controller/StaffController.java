package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.UserRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Staff API")
public class StaffController {
    private final MailService emailService;
    private final UserService userService;
    private final ServiceService serviceService;
    private final DentistService dentistService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final DentistScheduleService dentistScheduleService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final DentistRepository dentistRepository;
    private final TimeSlotService timeSlotService;

//----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Staff information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findClientByMail(mail);
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok(userDTO.getUserDTOFromUser(user));
    }

    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            Client user = userService.findByMail(userService.mailExtract()).orElse(null);
            if (user != null) {
                user.setMail(userDTO.getMail());
                user.setName(userDTO.getName());
                user.setPhone(userDTO.getPhone());
                user.setBirthday(userDTO.getBirthday());
                userService.updateUser(user);
            }
            return ResponseEntity.ok(userDTO);
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found user");
            logger.error("Not found user", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //---------------------------MANAGE DENTIST---------------------------


    @Operation(summary = "All Services in Clinic To choose")
    @GetMapping("/show-service")
    public ResponseEntity<?> getServices() {
        try {
            List<Services> services = serviceService.findAllServices();
            if (!services.isEmpty()) {
                return ResponseEntity.ok(services);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any service");
                logger.error("Not found any service");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Set Service for dentists")
    @PostMapping("/set-service/{dentistID}")
    public ResponseEntity<?> updateDentistService(@PathVariable String dentistID, @RequestParam String serviceID) {
        try {
            Dentist dentist = dentistService.findDentistByID(dentistID);
            Services service = serviceService.findServiceByID(serviceID);
            dentist.getServicesList().add(service);

            return ResponseEntity.ok(dentist);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList")
    public ResponseEntity<?> findAllDentistByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients;
            if (search != null && !search.isEmpty()) {
                clients = userService.searchDentistByStaff(mail, search);
            } else {
                clients = userService.findDentistByStaff(mail);
            }

            if (!clients.isEmpty()) {
                List<UserDTO> clientDTOs = clients.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setName(client.getName());
                            clientDTO.setBirthday(client.getBirthday());

                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any dentist user");
                logger.error("Not found any dentist user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList/{mail}")
    public ResponseEntity<?> findDentistInformationByStaff(@PathVariable("mail") String dentistMail) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(dentistMail);
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());

            List<Appointment> appointmentList = appointmentService.findAllAppointmentByDentist(client.getMail(), staff.getClinic());
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentEntity -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentEntity.getServices().getName());
                        appointment.setStatus(appointmentEntity.getStatus());
                        appointment.setTimeSlot(appointmentEntity.getTimeSlot().getStartTime());
                        if (appointmentEntity.getStaff() != null) {
                            if (appointmentEntity.getUser() != null) {
                                appointment.setUser(appointmentEntity.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentEntity.getDependent().getName());
                            }
                        } else {
                            if (appointmentEntity.getDependent() != null) {
                                appointment.setDependent(appointmentEntity.getDependent().getName());
                            } else {
                                appointment.setUser(appointmentEntity.getUser().getName());
                            }
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());

            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "show dentist list timeslot for choosing set schedule")
    @GetMapping("/show-set-schedule")
    public ResponseEntity<?> showSetDentistSchedule() {
        try {
            String mail = userService.mailExtract();
            List<Client> dentistList;
            List<UserDTO> dentistListDTO = new ArrayList<>();
            List<TimeSlotDTO> timeSlotDTOS = new ArrayList<>();
            dentistList = userService.findDentistByStaff(mail);
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (!dentistList.isEmpty()) {
                dentistListDTO = dentistList.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setName(client.getName());
                            clientDTO.setBirthday(client.getBirthday());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());
            }
            List<TimeSlot> timeSlotList = timeSlotService.findByClinic(staff.getClinic());
            if (!timeSlotList.isEmpty()) {
                timeSlotDTOS = timeSlotList.stream()
                        .map(timeSlot -> {
                            TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
                            timeSlotDTO.setStartTime(timeSlot.getStartTime());
                            timeSlotDTO.setSlotNumber(timeSlotDTO.getSlotNumber());
                            return timeSlotDTO;
                        }).collect(Collectors.toList());
            }
            SetScheduleRequestDTO setScheduleRequestDTO = new SetScheduleRequestDTO(dentistListDTO, timeSlotDTOS);
            return ResponseEntity.ok(setScheduleRequestDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Set Dentist Schedule")
    @PostMapping("/set-schedule")
    public ResponseEntity<?> setDentistSchedule(@RequestParam String dentistMail,
                                                @RequestParam LocalDate startDate,
                                                @RequestParam LocalDate endDate,
                                                @RequestParam int slotNumber) {
        try {
            String mail = userService.mailExtract();
            if (mail == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "Cannot find user with current mail"), HttpStatus.FORBIDDEN);
            }

            Staff staff = userService.findStaffByMail(mail);
            if (staff == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "You don't have permission to do this"), HttpStatus.FORBIDDEN);
            }

            Clinic clinic = staff.getClinic();
            if (clinic == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "Staff doesn't belong to any clinic"), HttpStatus.FORBIDDEN);
            }
            Dentist dentist = dentistRepository.findDentistByUserMail(dentistMail);
            dentistScheduleService.setDentistSchedule(dentist.getDentistID(), startDate, endDate, slotNumber, clinic.getClinicID());
            return ResponseEntity.ok("Schedule set successfully");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Delete Dentist Schedule")
    @DeleteMapping("/delete-schedule")
    public ResponseEntity<?> deleteDentistSchedule(@RequestParam String dentistID,
                                                   @RequestParam LocalDate workDate, @RequestParam int numSlot) {
        try {
            dentistScheduleService.deleteDentistSchedule(dentistID, workDate, numSlot);
            return ResponseEntity.ok("Schedule deleted successfully");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //---------------------------MANAGE CUSTOMER---------------------------


    @Operation(summary = "customerList")
    @GetMapping("/customerList")
    public ResponseEntity<?> findAllCustomerByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients;

            if (search != null && !search.isEmpty()) {
                clients = userService.searchCustomerInClinicByStaff(mail, search);
            } else {
                clients = userService.findCustomerInClinicByStaff(mail);
            }

            if (clients != null && !clients.isEmpty()) {
                List<UserDTO> clientDTOs = clients.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setBirthday(client.getBirthday());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any customer ");
                logger.error("Not found any customer ");
                return ResponseEntity.status(204).body(error);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Customer Info")
    @GetMapping("/customerList/{mail}")
    public ResponseEntity<?> findCustomerInformationByStaff(@PathVariable("mail") String customerMail) {
        try {
            String staffMail = userService.mailExtract();

            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(customerMail);
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());
            List<Appointment> appointmentList = appointmentService.customerAppointment(client.getUserID(), staffMail);
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentEntity -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentEntity.getServices().getName());
                        appointment.setStatus(appointmentEntity.getStatus());
                        appointment.setTimeSlot(appointmentEntity.getTimeSlot().getStartTime());
                        if (appointmentEntity.getStaff() != null) {
                            if (appointmentEntity.getUser() != null) {
                                appointment.setUser(appointmentEntity.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentEntity.getDependent().getName());
                            }
                        } else {
                            if (appointmentEntity.getDependent() != null) {
                                appointment.setDependent(appointmentEntity.getDependent().getName());
                            } else
                                appointment.setUser(appointmentEntity.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //---------------------------MANAGE APPOINTMENT---------------------------


    @Operation(summary = "Send mail for user")
    @PostMapping("/{notificationID}/send-email")
    public ResponseEntity<?> sendEmail(@PathVariable String notificationID,
                                       @RequestParam String mail,
                                       @RequestParam String subject,
                                       @RequestParam String text) {
        Notification optionalNotification = notificationService.findNotificationByIDAndStatus(notificationID, 0);
        if (optionalNotification != null) {

            emailService.sendSimpleMessage(mail, subject, text);
            optionalNotification.setStatus(1);

            notificationService.save(optionalNotification);
            return ResponseEntity.ok("Mail send successfully");
        } else {
            throw new IllegalArgumentException("Notification not found with ID: " + notificationID);
        }
    }


    @Operation(summary = "Get all notification in the clinic")
    @GetMapping("/notification")
    public ResponseEntity<?> receiveNotification() {
        try {
            String mail = userService.mailExtract();

            List<Notification> notice = notificationService.receiveNotice(mail);
            if (notice == null) {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Notification not found");
                logger.error("Notification not found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
            }

            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/booking/all-service")
    public ResponseEntity<?> getAllServiceByClinic(@RequestParam LocalDate workDate) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            List<Services> dentistService;
            dentistService = serviceService
                    .getServiceNotNullByDate(workDate, clinic).stream().toList();
            return ResponseEntity.ok(dentistService);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @Operation(summary = "Show available schedules")
    @GetMapping("/booking/available-schedules")
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedules(
            @RequestParam LocalDate workDate,
            @RequestParam String servicesId) {
        Staff staff = userService.findStaffByMail(userService.mailExtract());
        Clinic clinic = staff.getClinic();
        List<DentistSchedule> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinic.getClinicID()).stream().toList();

        List<AvailableSchedulesResponse> availableSchedulesResponses = new ArrayList<>();
        for (DentistSchedule i : dentistScheduleList) {
            AvailableSchedulesResponse availableSchedulesResponse = new AvailableSchedulesResponse();
            availableSchedulesResponse.setDentistName(i.getDentist().getUser().getName());
            availableSchedulesResponse.setStartTime(i.getTimeslot().getStartTime());
            availableSchedulesResponses.add(availableSchedulesResponse);
        }
        return ResponseEntity.ok(dentistScheduleList);
    }


    @Operation(summary = "Booking")
    @PostMapping("/booking/make-booking/{dentistScheduleId}")
    public ResponseEntity<?> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail, @RequestParam String serviceId) {
        try {
            Client client = userService.findClientByMail(userService.mailExtract());
            if (userService.findClientByMail(customerMail) != null) {
                DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
                if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
                    throw new Error("Over booked for today!");
                }

                if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).size() >= 10) {
                    throw new Error("Full appointment for this date!");
                }
                Appointment newAppointment = new Appointment();

                newAppointment.setStaff(client.getStaff());
                newAppointment.setUser(userService.findClientByMail(customerMail));
                newAppointment.setClinic(dentistSchedule.getClinic());
                newAppointment.setDate(dentistSchedule.getWorkDate());
                newAppointment.setTimeSlot(dentistSchedule.getTimeslot());
                newAppointment.setDentist(dentistSchedule.getDentist());
                newAppointment.setServices(serviceService.findServiceByID(serviceId));
                newAppointment.setDentistScheduleId(dentistScheduleId);
                newAppointment.setStatus(1);
                if (dependentID != null) {
                    Dependent dependent = userService.findDependentByDependentId(dependentID);
                    newAppointment.setDependent(dependent);
                }
                dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
                Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
                otherSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(0)));
                appointmentService.save(newAppointment);
                return ResponseEntity.ok("Booking successfully");

            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found");
                logger.error("Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PutMapping("/delete-booking/{appointmentId}")
    public ResponseEntity<?> deleteBooking(@PathVariable String appointmentId) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            String dentistScheduleId = appointment.getDentistScheduleId();
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            if (appointment.getStatus() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment has already been cancelled");
            }
            appointment.setStatus(0);
            Optional<List<DentistSchedule>> unavailableSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 0);
            unavailableSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(1)));
            appointmentService.save(appointment);
            return ResponseEntity.ok("Appointment has been cancelled");
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found");
            logger.error("Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Staff")
    @PatchMapping("/appointment-history/{appointmentid}")
    public ResponseEntity<?> setAppointmentStatus(@PathVariable("appointmentid") String appointmentId, @RequestParam("status") int status) {

        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/appointment-history")
    public ResponseEntity<?> appointmentHistoryByStaff(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList;
            if (date != null || (search != null && !search.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByStaff(date, search, mail);
            } else appointmentList = appointmentService.findAppointmentInClinic(mail);
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentEntity -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setAppointmentId(appointmentEntity.getAppointmentID());
                        appointment.setDate(appointmentEntity.getDate());
                        appointment.setServices(appointmentEntity.getServices().getName());
                        appointment.setDentist(appointmentEntity.getDentist().getUser().getName());
                        if (appointmentEntity.getStaff() != null)
                            appointment.setStaff(appointmentEntity.getStaff().getUser().getName());
                        appointment.setStatus(appointmentEntity.getStatus());
                        appointment.setTimeSlot(appointmentEntity.getTimeSlot().getStartTime());
                        if (appointmentEntity.getStaff() != null) {
                            if (appointmentEntity.getUser() != null) {
                                appointment.setUser(appointmentEntity.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentEntity.getDependent().getName());
                            }
                        } else {
                            if (appointmentEntity.getDependent() != null) {
                                appointment.setDependent(appointmentEntity.getDependent().getName());
                            } else
                                appointment.setUser(appointmentEntity.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            if(!appointmentDTOList.isEmpty()){
                return ResponseEntity.ok(appointmentDTOList);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No appointments found");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found");
            logger.error("Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Staff Dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoardData(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("year") int year) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, Integer> dailyAppointments = appointmentService.getDailyAppointmentsByDentist(date, staff);
            Map<Integer, Long> monthlyAppointments = appointmentService.getAppointmentsByStaffForYear(staff, year);
            int totalAppointmentInMonth = appointmentService.totalAppointmentsInMonthByStaff(staff);
            int totalAppointmentInYear = appointmentService.totalAppointmentsInYearByStaff(staff);

            DashboardResponse dashboardResponse = new DashboardResponse(dailyAppointments, monthlyAppointments, totalAppointmentInMonth, totalAppointmentInYear);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Data not found");
            logger.error("Data not found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }

    //---------------------------MANAGE CLINIC---------------------------

    @Operation(summary = "Get timetable for 1 specific date")
    @PostMapping("/timetable/{date}")
    public ResponseEntity<?> timeTable(@PathVariable("date") LocalDate date) {
        try {
            List<DentistSchedule> dentistSchedules = dentistScheduleService.findDentistScheduleByWorkDate(date);
            List<Appointment> appointments = appointmentService.findAppointmentsByDate(date);
            TimeTableResponseDTO timeTables = new TimeTableResponseDTO();
            List<TimeTableResponseDTO> timeTableResponseDTOList = timeTables.getTimeTableResponseDTOList(dentistSchedules, appointments);

            return ResponseEntity.ok(timeTableResponseDTOList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", e.getMessage());
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}

