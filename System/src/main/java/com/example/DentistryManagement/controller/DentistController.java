package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserAppointDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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


    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-today")
    public ResponseEntity<List<AppointmentDTO>> appointmentList() {
        try {
            String mail = userService.mailExtract();
            Optional<List<Appointment>> appointlist = appointmentService.findAppointmentByDentist(mail);
            if (appointlist.isPresent() && !appointlist.get().isEmpty()) {
                // Chuyển đổi danh sách Client sang danh sách ClientDTO
                List<AppointmentDTO> applist = appointlist.get().stream()
                        .map(appointmentriu -> {
                            AppointmentDTO appointment = new AppointmentDTO();
                            appointment.setServices(appointmentriu.getServices().getName());
                            appointment.setStatus(appointmentriu.getStatus());
                            appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                            if (appointmentriu.getStaff() != null) {
                                if (appointmentriu.getUser() != null) {
                                    appointment.setUser(appointmentriu.getUser().getName());
                                } else {
                                    appointment.setDependent(appointmentriu.getDependent().getName());
                                }
                            } else {
                                if (appointmentriu.getDependent() != null) {
                                    appointment.setDependent(appointmentriu.getDependent().getName());
                                } else
                                    appointment.setUser(appointmentriu.getUser().getName());
                            }

                            return appointment;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(applist);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/reminder")
    public ResponseEntity<Notification> reminderNotice(@RequestBody Notification notification) {
        Notification insertedNotification;
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
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
                    .map(appointmentriu -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentriu.getServices().getName());
                        appointment.setStatus(appointmentriu.getStatus());
                        appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                        if (appointmentriu.getStaff() != null) {
                            if (appointmentriu.getUser() != null) {
                                appointment.setUser(appointmentriu.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            }
                        } else {
                            if (appointmentriu.getDependent() != null) {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            } else
                                appointment.setUser(appointmentriu.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/weekSchedule")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDate(
            @RequestParam String start, @RequestParam String end) {
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
    }


    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-history/{appointmentid}")
    public ResponseEntity<Appointment> setAppointmentStatus(@RequestParam("status") int status, @PathVariable("appointmentid") String appointmentid) {

        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentid);
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-history/")
    public ResponseEntity<List<AppointmentDTO>> appointmentHistory(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String name) {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList = null;
            if (date != null || (name != null && !name.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByDentist(date, name, mail);
            } else {
                appointmentList = appointmentService.findAllAppointmentByDentist(userService.mailExtract());
            }
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentriu -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setAppointmentId(appointmentriu.getAppointmentID());
                        appointment.setServices(appointmentriu.getServices().getName());
                        appointment.setStatus(appointmentriu.getStatus());
                        appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                        if (appointmentriu.getStaff() != null) {
                            if (appointmentriu.getUser() != null) {
                                appointment.setUser(appointmentriu.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            }
                        } else {
                            if (appointmentriu.getDependent() != null) {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            } else
                                appointment.setUser(appointmentriu.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{status}")
    public ResponseEntity<Appointment> setAppointmentStatus(@PathVariable("status") int status, Appointment appointment) {
        try {
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
