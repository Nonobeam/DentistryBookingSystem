
package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.DTO.AvailableSchedulesResponse;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.repository.UserRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "User API")
public class UserController {

    private final UserService userService;
    private final DentistScheduleService dentistScheduleService;
    private final AppointmentService appointmentService;
    private final PasswordResetTokenService tokenService;
    private final UserRepository userRepository;
    private final ClinicService clinicService;
    private final AppointmentRepository appointmentRepository;
    private final Logger logger = LogManager.getLogger(UserController.class);
    private final ServiceService serviceService;
    private final UserMapping userMapping;

    //----------------------------------- CUSTOMER INFORMATION -----------------------------------


    @Operation(summary = "Customer information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findClientByMail(mail);
        return ResponseEntity.ok(userMapping.getUserDTOFromUser(user));
    }


    //----------------------------------- APPOINTMENT INFORMATION -----------------------------------

    @Operation(summary = "All Clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/dependentList")
    public ResponseEntity<?> getAllDependentByCustomer() {
        try {
            String mail = userService.mailExtract();
            if (mail == null || mail.isEmpty()) {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any customer ");
                logger.error("Not found any customer ");
                return ResponseEntity.status(204).body(error);
            }
            List<Dependent> dependentsList = userService.findDependentByCustomer(mail);
            return ResponseEntity.ok(dependentsList);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @PostMapping("/dependentNew")
    public ResponseEntity<?> createDependentByCustomer(@RequestBody Dependent dependent) {
        try {
            String mail = userService.mailExtract();
            dependent.setUser(userService.findClientByMail(mail));
            return ResponseEntity.ok(userService.saveDependent(dependent));
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }


    @Operation(summary = "All Clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/all-clinic")
    public ResponseEntity<List<Clinic>> getAllClinics() {
        try {
            return ResponseEntity.ok(clinicService.findAll());
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }


    @GetMapping("/all-service/{clinicID}")
    public ResponseEntity<?> getAllServiceByClinic(@RequestParam LocalDate bookDate,
                                                   @PathVariable String clinicID) {
        try {
            List<Services> dentistService;
            Clinic clinic = clinicService.findClinicByID(clinicID);
            dentistService = serviceService
                    .getServiceNotNullByDate(bookDate, clinic).stream().toList();
            return ResponseEntity.ok(dentistService);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }


    @Operation(summary = "Show available schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{clinicID}/available-schedules")
    public ResponseEntity<?> getAvailableSchedules(
            @RequestParam LocalDate workDate,
            @PathVariable String clinicID,
            @RequestParam String servicesId) {

        List<DentistSchedule> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinicID).stream().toList();

        List<AvailableSchedulesResponse> availableSchedulesResponses = new ArrayList<>();
        for (DentistSchedule i : dentistScheduleList) {
            AvailableSchedulesResponse availableSchedulesResponse = new AvailableSchedulesResponse();
            availableSchedulesResponse.setDentistScheduleID(i.getScheduleID());
            availableSchedulesResponse.setDentistName(i.getDentist().getUser().getName());
            availableSchedulesResponse.setStartTime(i.getTimeslot().getStartTime());
            availableSchedulesResponses.add(availableSchedulesResponse);
        }

        return ResponseEntity.ok(availableSchedulesResponses);
    }


    @Operation(summary = "Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/booking/{dentistScheduleId}")
    public ResponseEntity<?> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID, @RequestParam String serviceId) {
        try {
            Client customer = userService.findClientByMail(userService.mailExtract());
            Dependent dependent = dependentID != null ? userService.findDependentByDependentId(dependentID) : null;
            Services services = serviceService.findServiceByID(serviceId);
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);

            if (customer == null || customer.getStatus() == 0) {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found in system");
                logger.error("Customer not found in system");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            if (dentistSchedule == null || dentistSchedule.getAvailable() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("400", "Dentist Schedule not found"));
            } else if (dentistSchedule.getWorkDate().isBefore(LocalDate.now())) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "The booking must be in the future"));
            }

            if (services == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("400", "Service not found"));
            }

            if (appointmentService.findAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "Reach the limit of personal appointment. 5/5"));
            }

            if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).size() >= 10) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "You cannot book another appointment right now. The clinic is full right now!"));
            }

            appointmentService.createAppointment(null, customer, dentistSchedule, services, dependent);
            return ResponseEntity.ok("Booking successfully");
        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Delete appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/delete-booking/{appointmentId}")
    public ResponseEntity<?> deleteBooking(@PathVariable String appointmentId) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            String dentistScheduleId = appointment.getDentistScheduleId();
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            //Check for duplicate cancelled just in case
            if (appointment.getStatus() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment has already been cancelled");
            }
            appointment.setStatus(0);
            Optional<List<DentistSchedule>> unavailableSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 0);
            unavailableSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(1)));
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment has been cancelled");
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("403", "Appointment can not be deleted");
            logger.error("Appointment can not be deleted", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Show user Appointment history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-history")
    public ResponseEntity<?> getAppointmentHistory
            (@RequestParam(required = false) LocalDate workDate,
             @RequestParam(required = false) Integer status) {
        try {
            Client user = userService.findClientByMail(userService.mailExtract());
            List<Appointment> appointmentList = appointmentService.findAppointmentHistory(user, workDate, status);
            return ResponseEntity.ok(appointmentList);
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

    //----------------------------------- UPDATE INFORMATION -----------------------------------


    @Operation(summary = "Send a reset password link to customer's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword() {
        try {
            Client user = userService.findClientByMail(userService.mailExtract());
            if (user != null) {
                String token = UUID.randomUUID().toString();
                tokenService.createPasswordResetTokenForUser(user, token);
                tokenService.sendPasswordResetEmail(user.getMail(), token);
            }
            return ResponseEntity.ok("Password reset link has been sent to your email");
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


    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            userRepository.findByMail(userService.mailExtract()).ifPresent(userMapping::getUserDTOFromUser);
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

    @Operation(summary = "Reset customer's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestParam("password") String password) {
        try {
            String validationResult = tokenService.validatePasswordResetToken(token);
            if (validationResult.equalsIgnoreCase("invalid")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }
            tokenService.resetPassword(token, password);
            return ResponseEntity.ok("Password has been reset successfully");
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


}
