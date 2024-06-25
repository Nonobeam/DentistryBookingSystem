package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserAppointDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/dentist")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Dentist API")
public class DentistController {
    private final UserService userService;
    private final DentistService dentistService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Operation(summary = "Dentist")
    @GetMapping("/appointment-today")
    public ResponseEntity<?> appointmentList() {
        ErrorResponseDTO error = new ErrorResponseDTO();
        try {
            String mail = userService.mailExtract();
            Optional<List<Appointment>> appointlist = appointmentService.findAppointmentByDentist(mail);
            if (appointlist.isPresent() && !appointlist.get().isEmpty()) {
                List<AppointmentDTO> applist = appointlist.get().stream()
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

                return ResponseEntity.ok(applist);
            } else {
                error.setCode("204");
                error.setMessage("Not found any appointment");
                logger.error("Not found any appointment");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {

            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Dentist")
    @PostMapping("/reminder")
    public ResponseEntity<?> reminderNotice(@RequestBody Notification notification) {
        Notification insertedNotification;
        ErrorResponseDTO error = new ErrorResponseDTO();

        try {
            if (notification != null) {
                Client client = userService.findClientByMail(userService.mailExtract());
                Dentist dentist = dentistService.findDentistByID(client.getUserID());
                notification.setDentist(dentist);
                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();
                notification.setDate(Date.valueOf(currentDate));
                notification.setTime(Time.valueOf(currentTime));
                notification.setStatus(0);

                insertedNotification = notificationService.insertNotification(notification);
                return ResponseEntity.ok(insertedNotification);
            } else {
                error.setCode("204");
                error.setMessage("Not found any notification");
                logger.error("Not found any notification");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")
    @GetMapping("/customer/{mail}")
    public ResponseEntity<?> findAllCustomerByDentist(@PathVariable("mail") String customerMail) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(customerMail);
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());
            Optional<List<Appointment>> appointmentList = appointmentService.customerAppointmentfollowdentist(client.getUserID(), userService.mailExtract());
            List<AppointmentDTO> appointmentDTOList = appointmentList.get().stream()
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

    @Operation(summary = "Dentist")
    @GetMapping("/weekSchedule")
    public ResponseEntity<?> getAppointmentsForDate(
            @RequestParam String start, @RequestParam String end) {
        try {
            LocalDate startOfWeek = LocalDate.parse(start);
            LocalDate endOfWeek = LocalDate.parse(end);
            List<Appointment> appointments = appointmentService.getAppointmentsForWeek(startOfWeek, endOfWeek);

            List<AppointmentDTO> appointmentDTOList = appointments.stream()
                    .map(appointment -> {
                        AppointmentDTO appointmentDTO = new AppointmentDTO();
                        appointmentDTO.setAppointmentId(appointment.getAppointmentID());
                        appointmentDTO.setServices(appointment.getServices().getName());
                        appointmentDTO.setStatus(appointment.getStatus());
                        appointmentDTO.setTimeSlot(appointment.getTimeSlot().getStartTime());

                        if (appointment.getStaff() != null) {
                            if (appointment.getUser() != null) {
                                appointmentDTO.setUser(appointment.getUser().getName());
                            } else {
                                appointmentDTO.setDependent(appointment.getDependent().getName());
                            }
                        } else {
                            if (appointment.getDependent() != null) {
                                appointmentDTO.setDependent(appointment.getDependent().getName());
                            } else {
                                appointmentDTO.setUser(appointment.getUser().getName());
                            }
                        }

                        return appointmentDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(appointmentDTOList);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")

    @GetMapping("/appointment-history/{appointmentid}")
    public ResponseEntity<?> setAppointmentStatus(@RequestParam("status") int status, @PathVariable("appointmentid") String appointmentid) {

        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentid);
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

    @Operation(summary = "Dentist")

    @GetMapping("/appointment-history/")
    public ResponseEntity<?> appointmentHistory(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String name) {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList = null;
            if (date != null || (name != null && !name.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByDentist(date, name, mail);
            } else {
                appointmentList = appointmentService.findAllAppointmentByDentist(userService.mailExtract());
            }
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
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Dentist")

    @PutMapping("/{status}")
    public ResponseEntity<?> setAppointmentStatus(@PathVariable("status") int status, Appointment appointment) {
        try {
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
}
