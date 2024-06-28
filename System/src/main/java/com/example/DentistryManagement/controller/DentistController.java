package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.AdminDTO;
import com.example.DentistryManagement.DTO.UserAppointDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final UserRepository userRepository;

//----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Dentist information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findClientByMail(mail);
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok(userDTO.getUserDTOFromUser(user));
    }

    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody AdminDTO userDTO) {
        try {
            Client user = userRepository.findByMail(userService.mailExtract()).orElse(null);
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


    @Operation(summary = "Dentist")
    @GetMapping("/appointment-today")
    public ResponseEntity<?> appointmentList() {
        List<AppointmentDTO> applist = new ArrayList<>();
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointlist = appointmentService.findAppointmentByDentist(mail);
            if (!appointlist.isEmpty()) {
                applist = appointlist.stream()
                        .map(appointmentEntity -> {
                            AppointmentDTO appointment = new AppointmentDTO();
                            appointment.setServices(appointmentEntity.getServices().getName());
                            appointment.setStatus(appointmentEntity.getStatus());
                            appointment.setDate(appointmentEntity.getDate());
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
                        .collect(Collectors.toList());

            }
            return ResponseEntity.ok(applist);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Dentist")
    @PostMapping("/reminder")
    public ResponseEntity<?> reminderNotice(@RequestBody Notification notification) {
        Notification insertedNotification = new Notification();
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

            }
            return ResponseEntity.ok(insertedNotification);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")
    @GetMapping("/customer/{customerID}")
    public ResponseEntity<?> findAllCustomerByDentist(@PathVariable("customerID") String customerID) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findUserById(customerID);
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
                        appointment.setDate(appointmentEntity.getDate());
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

    @Operation(summary = "Dentist")
    @GetMapping("/weekSchedule")
    public ResponseEntity<?> getAppointmentsForDate(
            @RequestParam LocalDate start, @RequestParam LocalDate end) {
        try {
            Dentist dentist = userService.findDentistByMail(userService.mailExtract());
            List<Appointment> appointments = appointmentService.getAppointmentsForWeek(start, end, dentist);

            List<AppointmentDTO> appointmentDTOList = appointments.stream()
                    .map(appointmentEntity -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentEntity.getServices().getName());
                        appointment.setStatus(appointmentEntity.getStatus());
                        appointment.setTimeSlot(appointmentEntity.getTimeSlot().getStartTime());
                        appointment.setDate(appointmentEntity.getDate());
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
                            } else {
                                appointment.setUser(appointmentEntity.getUser().getName());
                            }
                        }
                        return appointment;
                    })
                    .toList();

            Map<LocalDate, List<AppointmentDTO>> appointmentMapByDate = appointmentDTOList.stream()
                    .collect(Collectors.groupingBy(AppointmentDTO::getDate));

            return ResponseEntity.ok(appointmentMapByDate);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")

    @GetMapping("/appointment-history/{appointmentid}")
    public ResponseEntity<?> setAppointmentStatus(@RequestParam("status") int status, @PathVariable("appointmentid") String appointmentid) {

        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentid);
            appointment.setStatus(status);
            appointment = appointmentService.AppointmentUpdate(appointment);
            return ResponseEntity.ok(appointment);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")

    @GetMapping("/appointment-history/")
    public ResponseEntity<?> appointmentHistory(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String name) {
        try {
            String mail = userService.mailExtract();
            Dentist dentist = userService.findDentistByMail(mail);
            List<Appointment> appointmentList = null;
            if (date != null || (name != null && !name.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByDentist(date, name, dentist);
            } else {
                appointmentList = appointmentService.findAllAppointmentByDentist(dentist.getUser().getMail(), dentist.getClinic());
            }
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentEntity -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setAppointmentId(appointmentEntity.getAppointmentID());
                        appointment.setServices(appointmentEntity.getServices().getName());
                        appointment.setStatus(appointmentEntity.getStatus());
                        appointment.setDate(appointmentEntity.getDate());
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
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}