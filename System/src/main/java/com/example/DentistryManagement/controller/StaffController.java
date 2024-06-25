package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
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
    private final AppointmentRepository appointmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


//---------------------------MANAGE DENTIST---------------------------

    @Operation(summary = "All Services in System")
    @GetMapping("/set-service/{dentistID}")
    public ResponseEntity<?> updateDentistService(@PathVariable String dentistID, @RequestParam String serviceID) {
        Dentist dentist;
        Services service;
        try {
            dentist = dentistService.findDentistByID(dentistID);
            service = serviceService.findServiceByID(serviceID);
            dentist.getServicesList().add(service);

            return ResponseEntity.ok(dentist);
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList")
    public ResponseEntity<?> findAllDentistByStaff(@RequestParam(required = false) String search) {
        ErrorResponseDTO error = new ErrorResponseDTO();

        try {
            String mail = userService.mailExtract();
            List<Client> clientsOptional = null;
            if (search != null && !search.isEmpty()) {
                clientsOptional = userService.searchDentistByStaff(mail, search);

            } else clientsOptional = userService.findDentistByStaff(mail);

            if (!clientsOptional.isEmpty()) {
                List<UserDTO> clientDTOs = clientsOptional.stream()
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
                error.setCode("204");
                error.setMessage("Not found any staff user");
                logger.error("Not found any staff user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Staff")
    @GetMapping("/dentistList/{mail}")
    public ResponseEntity<?> findDentistInformationByStaff(@PathVariable("mail") String dentistMail) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(dentistMail);

            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());

            List<Appointment> appointmentList = appointmentService.findAllAppointmentByDentist(client.getUserID());
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


    @Operation(summary = "Set Dentist Schedule")

    @PostMapping("/set-schedule")
    public ResponseEntity<?> setDentistSchedule(@RequestParam String dentistID,
                                                @RequestParam LocalDate startDate,
                                                @RequestParam LocalDate endDate,
                                                @RequestParam String timeSlotID) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            dentistScheduleService.setDentistSchedule(dentistID, startDate, endDate, timeSlotID, clinic.getClinicID());
            return ResponseEntity.ok("Schedule set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @Operation(summary = "Delete Dentist Schedule")
    @DeleteMapping("/delete-schedule")
    public ResponseEntity<?> deleteDentistSchedule(@RequestParam String dentistID,
                                                   @RequestParam LocalDate workDate) {
        try {
            dentistScheduleService.deleteDentistSchedule(dentistID, workDate);
            return ResponseEntity.ok("Schedule deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //---------------------------MANAGE CUSTOMER---------------------------


    @Operation(summary = "customerList")
    @GetMapping("/customerList")
    public ResponseEntity<?> findAllCustomerByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients = null;

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
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Customer Info")
    @GetMapping("/customerList/{mail}")
    public ResponseEntity<?> findCustomerByStaff(@PathVariable("mail") String customerMail) {
        try {
            String mail = userService.mailExtract();

            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(customerMail);
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());
            List<Appointment> appointmentList = appointmentService.customerAppointment(client.getUserID(), mail);
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
            Notification notification = optionalNotification;

            emailService.sendSimpleMessage(mail, subject, text);
            notification.setStatus(1);

            notificationService.save(notification);
            return ResponseEntity.ok("Mail send successfully");
        } else {
            throw new IllegalArgumentException("Notification not found with ID: " + notificationID);
        }
    }


    @Operation(summary = "Get all notification in the clinic")
    @GetMapping()
    public ResponseEntity<?> receiveNotification() {
        ErrorResponseDTO error = new ErrorResponseDTO();
        try {
            String mail = userService.mailExtract();

            List<Notification> notice = notificationService.receiveNotice(mail);
            if (notice == null) {
                error.setCode("204");
                error.setMessage("Notification not found");
                logger.error("Notification not found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
            }

            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
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
    public ResponseEntity<?> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail) {
        ErrorResponseDTO error = new ErrorResponseDTO();

        try {
            Client client = userService.findClientByMail(userService.mailExtract());
            if (userService.findClientByMail(customerMail) != null) {
                DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
                if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
                    throw new Error("Over booked for today!");
                }

                if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
                    throw new Error("Full appointment for this date!");
                }
                Appointment newAppointment = new Appointment();

                newAppointment.setStaff(client.getStaff());
                newAppointment.setUser(userService.findClientByMail(customerMail));
                newAppointment.setClinic(dentistSchedule.getClinic());
                newAppointment.setDate(dentistSchedule.getWorkDate());
                newAppointment.setTimeSlot(dentistSchedule.getTimeslot());
                newAppointment.setDentist(dentistSchedule.getDentist());
                newAppointment.setDentistScheduleId(dentistScheduleId);
                newAppointment.setStatus(1);
                if (dependentID != null) {
                    Dependent dependent = userService.findDependentByDependentId(dependentID);
                    newAppointment.setDependent(dependent);
                }
                dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
                Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
                otherSchedule.ifPresent(schedules -> {
                    schedules.forEach(schedule -> schedule.setAvailable(0));
                });
                appointmentRepository.save(newAppointment);
                return ResponseEntity.ok(newAppointment);

            } else {
                error.setCode("204");
                error.setMessage("customer not found");
                logger.error("customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PutMapping("/delete-booking/{appointmentId}")
    public ResponseEntity<?> deleteBooking(@PathVariable String appointmentId) {
        ErrorResponseDTO error = new ErrorResponseDTO();

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
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment has been cancelled");
        } catch (Error e) {
            error.setCode("204");
            error.setMessage("customer not found");
            logger.error("customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/update-booking/{appointmentId}/all-service")
    public ResponseEntity<?> getAllServiceToUpdateByClinic(@RequestParam LocalDate workDate, @PathVariable("appointmentId") String appointmentId) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            List<Services> dentistService;
            dentistService = serviceService
                    .getServiceNotNullByDate(workDate, clinic).stream().toList();
            dentistService.add(appointment.getServices());
            return ResponseEntity.ok(dentistService);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }


    @Operation(summary = "Show available schedules")
    @GetMapping("/update-booking/{appointmentId}/available-schedules")
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedulesToUpdate(
            @RequestParam LocalDate workDate,
            @PathVariable String appointmentId,
            @RequestParam String servicesId) {
        Appointment appointment = appointmentService.findAppointmentById(appointmentId);
        List<DentistSchedule> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, appointment.getClinic().getClinicID()).stream().toList();

        List<AvailableSchedulesResponse> availableSchedulesResponses = new ArrayList<>();
        for (DentistSchedule i : dentistScheduleList) {
            AvailableSchedulesResponse availableSchedulesResponse = new AvailableSchedulesResponse();
            availableSchedulesResponse.setDentistScheduleID(i.getScheduleID());
            availableSchedulesResponse.setDentistName(i.getDentist().getUser().getName());
            availableSchedulesResponse.setStartTime(i.getTimeslot().getStartTime());
            availableSchedulesResponses.add(availableSchedulesResponse);
        }
        return ResponseEntity.ok(dentistScheduleList);
    }


    @Operation(summary = "Booking")
    @PostMapping("/update-booking/{appointmentId}/{dentistScheduleId}")

    public ResponseEntity<?> makeBookingToUpdate(@PathVariable String dentistScheduleId, @PathVariable String appointmentId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail, @RequestParam String serviceID ) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        try {
            Client client = userService.findClientByMail(userService.mailExtract());
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            deleteBooking(appointmentId);
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
                throw new Error("Over booked for today!");
            }

            if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
                throw new Error("Full appointment for this date!");
            }
            Appointment newAppointment = new Appointment();
            newAppointment.setStaff(client.getStaff());
            newAppointment.setUser(userService.findClientByMail(customerMail));
            newAppointment.setClinic(dentistSchedule.getClinic());
            newAppointment.setDate(dentistSchedule.getWorkDate());
            newAppointment.setServices(serviceService.findServiceByID(serviceID));
            newAppointment.setTimeSlot(dentistSchedule.getTimeslot());
            newAppointment.setDentist(dentistSchedule.getDentist());
            newAppointment.setDentistScheduleId(dentistScheduleId);
            newAppointment.setStatus(1);
            if (dependentID != null) {
                Dependent dependent = userService.findDependentByDependentId(dependentID);
                newAppointment.setDependent(dependent);
            }
            dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
            Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
            otherSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(0)));
            appointmentRepository.save(newAppointment);
            return ResponseEntity.ok(newAppointment);
        } catch (Error e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
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
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
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
            } else appointmentList = appointmentService.findApointmentClinic(mail);
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentEntity -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setAppointmentId(appointmentEntity.getAppointmentID());
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
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("204");
            error.setMessage("Not found any ");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoardData(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("year") int year) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, List<Appointment>> dailyAppointments = appointmentService.getDailyAppointmentsByDentist(date, staff);
            Map<Integer, Long> monthlyAppointments = appointmentService.getAppointmentsByStaffForYear(staff, year);
            int totalAppointmentInMonth = appointmentService.totalAppointmentsInMonthByStaff(staff);
            int totalAppointmentInYear = appointmentService.totalAppointmentsInYearByStaff(staff);

            DashboardResponse dashboardResponse = new DashboardResponse(dailyAppointments, monthlyAppointments, totalAppointmentInMonth, totalAppointmentInYear);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("204");
            error.setMessage("Not found data in dashboard");
            logger.error("Not found data in dashboard");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
