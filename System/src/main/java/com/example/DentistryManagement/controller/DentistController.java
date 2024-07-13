package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.repository.UserRepository;
import com.example.DentistryManagement.service.*;
import com.example.DentistryManagement.service.AppointmentService.AppointmentAnalyticService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentService;
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
import java.util.*;
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
    private final DentistScheduleService dentistScheduleService;
    private final UserMapping userMapping;
    private final AppointmentAnalyticService appointmentAnalyticService;

//----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Dentist information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findClientByMail(mail);
        return ResponseEntity.ok(userMapping.getUserDTOFromUser(user));
    }

    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            Client user = userRepository.findByMail(userService.mailExtract()).orElse(null);
            if (user != null) {
                userService.updateUser(userDTO, user);
            } else {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "Cannot find user"));
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
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointlist = appointmentAnalyticService.getAppointmentsByDentistMail(mail);
            List<AppointmentDTO> appointmentDtoList = new ArrayList<>();
            if (!appointlist.isEmpty()) {
                appointmentDtoList = appointmentService.appointmentDTOList(appointlist);
            }
            if (!appointmentDtoList.isEmpty()) {
                return ResponseEntity.ok(appointmentDtoList);
            } else return ResponseEntity.ok("Not found any appointment today");

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
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userDTO.setBirthday(client.getBirthday());
            List<Appointment> appointmentList = appointmentAnalyticService.customerAppointmentFollowDentist(client.getUserID(), userService.mailExtract());
            if (!appointmentList.isEmpty()) {
                List<AppointmentDTO> appointmentDtoList = appointmentService.appointmentDTOList(appointmentList);
                userAppointDTO.setAppointment(appointmentDtoList);
            }
            userAppointDTO.setUserDTO(userDTO);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")
    @GetMapping("/weekSchedule/{startDate}")
    public Object getAppointmentsForDate(@PathVariable LocalDate startDate, @RequestParam int numDay) {
        try {
            Dentist dentist = dentistService.findDentistByMail(userService.mailExtract());
            Map<LocalDate, List<TimeTableResponseDTO>> timeTableResponseMap = new HashMap<>();
            startDate.datesUntil(startDate.plusDays(numDay).plusDays(1)).forEach(currentDate -> {
                List<DentistSchedule> dentistSchedules = dentistScheduleService.findDentistScheduleByWorkDateByDentist(startDate, numDay, dentist).stream()
                        .filter(schedule -> schedule.getWorkDate().equals(currentDate))
                        .sorted(Comparator.comparing(schedule -> schedule.getTimeslot().getStartTime()))
                        .collect(Collectors.toList());
                List<Appointment> appointments = appointmentAnalyticService.findAppointmentsByDateBetweenDentist(startDate, startDate.plusDays(numDay), dentist).stream()
                        .filter(appointment -> appointment.getDate().equals(currentDate))
                        .sorted(Comparator.comparing(appointment -> appointment.getTimeSlot().getStartTime()))
                        .collect(Collectors.toList());
                TimeTableResponseDTO timeTableResponseDTO = new TimeTableResponseDTO();
                List<TimeTableResponseDTO> timeTableResponseDTOList = timeTableResponseDTO.getTimeTableResponseDTOList(dentistSchedules, appointments);
                timeTableResponseMap.put(currentDate, timeTableResponseDTOList);
            });

            return ResponseEntity.ok(timeTableResponseMap);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Dentist")

    @GetMapping("/appointment-history/{appointmentId}")
    public ResponseEntity<?> setAppointmentStatus(@RequestParam int status, @PathVariable String appointmentId) {

        try {
            Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentId);
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
    public ResponseEntity<?> appointmentHistory(@RequestParam(required = false) String name) {
        try {
            String mail = userService.mailExtract();
            Dentist dentist = userService.findDentistByMail(mail);
            List<Appointment> appointmentList;
            if (name != null && !name.isEmpty()) {
                appointmentList = appointmentAnalyticService.getAppointmentsByCustomerNameOrDependentNameAndByDentist(name, dentist);
            } else {
                appointmentList = appointmentAnalyticService.getAppointmentsInAClinicByCustomerMail(dentist.getUser().getMail(), dentist.getClinic());
            }
            List<AppointmentDTO> appointmentDtoList = appointmentService.appointmentDTOList(appointmentList);
            if (!appointmentDtoList.isEmpty()) {
                return ResponseEntity.ok(appointmentDtoList);
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found any appointment");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


}