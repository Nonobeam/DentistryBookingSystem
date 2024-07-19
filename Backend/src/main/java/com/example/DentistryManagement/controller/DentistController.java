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
import com.example.DentistryManagement.service.AppointmentService.AppointmentUpdateService;
import com.example.DentistryManagement.service.UserService.UserDentistService;
import com.example.DentistryManagement.service.UserService.UserService;
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
    private final UserDentistService dentistService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserRepository userRepository;
    private final DentistScheduleService dentistScheduleService;
    private final UserMapping userMapping;
    private final AppointmentAnalyticService appointmentAnalyticService;
    private final AppointmentUpdateService appointmentUpdateService;

//----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Dentist information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findUserByMail(mail);
        return ResponseEntity.ok(userMapping.getUserDTOFromUser(user));
    }

    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            Client currentUser = userService.findUserByMail(userService.mailExtract());
            if (currentUser == null) {
                return ResponseEntity.status(403).body(new ErrorResponseDTO("403", "Cannot find user"));
            }
            userService.updateUser(userDTO, currentUser);
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

    @Operation(summary = "Dentist clinic")
    @GetMapping("/clinic")
    public ResponseEntity<String> clinicName() {
        String mail = userService.mailExtract();
        Client user = userService.findUserByMail(mail);
        return ResponseEntity.ok(user.getDentist().getClinic().getName() + " - " + user.getDentist().getClinic().getAddress());
    }

    @Operation(summary = "Get today appointment")
    @GetMapping("/appointment-today")
    public ResponseEntity<?> appointmentList() {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList = appointmentAnalyticService.getAppointmentsByDentistMail(mail);
            List<AppointmentDTO> appointmentDtoList = new ArrayList<>();
            if (!appointmentList.isEmpty()) {
                appointmentDtoList = appointmentService.appointmentDTOList(appointmentList);
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


    @Operation(summary = "Create dentist notice")
    @PostMapping("/reminder")
    public ResponseEntity<?> reminderNotice(@RequestBody Notification notification) {
        Notification newNotification = new Notification();
        try {
            if (notification != null) {
                Client client = userService.findUserByMail(userService.mailExtract());
                Dentist dentist = dentistService.findDentistByID(client.getUserID());

                notification.setDentist(dentist);
                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();
                notification.setDate(Date.valueOf(currentDate));
                notification.setTime(Time.valueOf(currentTime));
                notification.setStatus(0);


                newNotification = notificationService.insertNotification(notification);

            }
            return ResponseEntity.ok(newNotification);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Get customer list by dentist")
    @GetMapping("/customer/{customerID}")
    public ResponseEntity<?> findAllCustomerByDentist(@PathVariable("customerID") String customerID) {
        try {
            Client customer = userService.findUserById(customerID);

            UserDTO customerDTO = new UserDTO();
            customerDTO.setName(customer.getName());
            customerDTO.setPhone(customer.getPhone());
            customerDTO.setMail(customer.getMail());
            customerDTO.setBirthday(customer.getBirthday());

            UserAppointDTO customerAppointmentDTO = new UserAppointDTO();

            List<Appointment> appointmentList = appointmentAnalyticService.customerAppointmentFollowDentist(customer.getUserID(), userService.mailExtract());
            if (!appointmentList.isEmpty()) {
                List<AppointmentDTO> appointmentDTOList = appointmentService.appointmentDTOList(appointmentList);
                customerAppointmentDTO.setAppointment(appointmentDTOList);
            }
            customerAppointmentDTO.setUserDTO(customerDTO);
            return ResponseEntity.ok(customerAppointmentDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Get dentist weekly schedule")
    @GetMapping("/weekSchedule/{startDate}")
    public Object getAppointmentsForDate(@PathVariable LocalDate startDate, @RequestParam int numDay) {
        try {
            Dentist dentist = dentistService.findDentistByMail(userService.mailExtract());
            Map<LocalDate, List<TimeTableResponseDTO>> timeTableResponseMap = new HashMap<>();

            startDate.datesUntil(startDate.plusDays(numDay).plusDays(1)).forEach(currentDate -> {

                List<DentistSchedule> dentistScheduleList = dentistScheduleService.findDentistScheduleByWorkDateByDentist(startDate, numDay, dentist).stream()
                        .filter(schedule -> schedule.getWorkDate().equals(currentDate))
                        .sorted(Comparator.comparing(schedule -> schedule.getTimeslot().getStartTime()))
                        .collect(Collectors.toList());

                List<Appointment> appointmentList = appointmentAnalyticService.findAppointmentsByDateAndDentist(startDate, startDate.plusDays(numDay), dentist).stream()
                        .filter(appointment -> appointment.getDate().equals(currentDate))
                        .sorted(Comparator.comparing(appointment -> appointment.getTimeSlot().getStartTime()))
                        .collect(Collectors.toList());

                TimeTableResponseDTO timeTableResponseDTO = new TimeTableResponseDTO();
                List<TimeTableResponseDTO> timeTableResponseDTOList = timeTableResponseDTO.getTimeTableResponseDTOList(dentistScheduleList, appointmentList);
                timeTableResponseMap.put(currentDate, timeTableResponseDTOList);
            });

            return ResponseEntity.ok(timeTableResponseMap);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Update Appointment History")

    @PatchMapping("/appointment-history/{appointmentId}")
    public ResponseEntity<?> updateAppointmentStatus(@RequestParam int status, @PathVariable("appointmentId") String appointmentId) {

        try {
            Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentId);
            appointment.setStatus(status);
            appointment = appointmentUpdateService.UpdateAppointment(appointment);
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
            Dentist dentist = dentistService.findDentistByMail(mail);
            List<Appointment> appointmentList;
            if (name != null && !name.isEmpty()) {
                appointmentList = appointmentAnalyticService.getAppointmentsByCustomerNameOrDependentNameAndDentist(name, dentist);
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