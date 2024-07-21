
package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.DTO.AppointmentFeedbackDTO;
import com.example.DentistryManagement.DTO.AvailableSchedulesResponse;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.service.*;
import com.example.DentistryManagement.service.AppointmentService.AppointmentAnalyticService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentBookingService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentDeleteService;
import com.example.DentistryManagement.service.UserService.UserDependentService;
import com.example.DentistryManagement.service.UserService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class UserController {

    private final UserService userService;
    private final UserDependentService userDependentService;
    private final UserMapping userMapping;
    private final ClinicService clinicService;
    private final ServiceService serviceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final DentistScheduleService dentistScheduleService;
    private final Logger logger = LogManager.getLogger(UserController.class);
    private final AppointmentAnalyticService appointmentAnalyticService;
    private final AppointmentBookingService appointmentBookingService;
    private final AppointmentDeleteService appointmentDeleteService;
    private final AppointmentRepository appointmentRepository;


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
        Client user = userService.findUserByMail(mail);
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
            List<Dependent> dependentsList = userDependentService.findDependentByCustomer(mail);
            return ResponseEntity.ok(dependentsList);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @PostMapping("/dependentNew")
    public ResponseEntity<?> createDependentByCustomer(@RequestBody Dependent dependent) {
        try {
            String mail = userService.mailExtract();
            dependent.setUser(userService.findUserByMail(mail));
            return ResponseEntity.ok(userDependentService.saveDependent(dependent));
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
            return ResponseEntity.ok(clinicService.findAllClinicsByStatus(1));
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
            long durationInMinutes = i.getClinic().getSlotDuration().toSecondOfDay() / 60;
            availableSchedulesResponse.setEndTime(availableSchedulesResponse.getStartTime().plusMinutes(durationInMinutes));
            availableSchedulesResponses.add(availableSchedulesResponse);
        }

        return ResponseEntity.ok(availableSchedulesResponses.stream().sorted(Comparator.comparing(AvailableSchedulesResponse::getStartTime)).collect(Collectors.toList()));
    }

    @Operation(summary = "Check maxed booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/booking")
    public boolean checkMaxedBooking() {
        Client customer = userService.findUserByMail(userService.mailExtract());
        return appointmentAnalyticService.getAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5;
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
        // Apply redis single-thread
        String lockKey = "booking:lock:" + dentistScheduleId;
        boolean lockAcquired = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS));
        if (!lockAcquired) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO("409", "Booking in progress by another user"));
        }

        try {
            // Current user
            Client customer = userService.findUserByMail(userService.mailExtract());
            Dependent dependent = dependentID != null ? userDependentService.findDependentByDependentId(dependentID) : null;
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

            if (appointmentAnalyticService.getAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "Reach the limit of personal appointment. 5/5"));
            }

            if (appointmentAnalyticService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).size() >= 10) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "You cannot book another appointment right now. The clinic is full right now!"));
            }

            appointmentBookingService.createAppointment(null, customer, dentistSchedule, services, dependent);
            return ResponseEntity.ok("Booking successfully");
        } catch (Error e) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponseDTO);
        } finally {
            redisTemplate.delete(lockKey);
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
            appointmentDeleteService.deleteAppointment(appointmentId);
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
            Client user = userService.findUserByMail(userService.mailExtract());
            List<Appointment> appointmentList = appointmentAnalyticService.getAppointmentsByUserAndByDateOrStatus(user, workDate, status);
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

    @Operation(summary = "Show user Un feedback appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-feedback")
    public ResponseEntity<?> getAppointmentFeedback
            () {
        try {
            Client user = userService.findUserByMail(userService.mailExtract());
            List<Appointment> appointmentList = appointmentAnalyticService.getAppointmentByUnFeedback(user);
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

    @Operation(summary = "feedback appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/appointment-feedback/{appointmentID}")
    public ResponseEntity<?> putAppointmentFeedback
            (@PathVariable String appointmentID, @RequestBody AppointmentFeedbackDTO appointmentDTO) {
        try {
            Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentID);
            appointment.setFeedback(appointmentDTO.getFeedback());
            appointment.setStarAppointment(appointmentDTO.getStarAppointment());
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Feedback successfully");
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

    @Operation(summary = "feedback appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-feedback/{appointmentID}")
    public ResponseEntity<?> getAppointmentFeedback
            (@PathVariable String appointmentID) {
        try {
            Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentID);
            AppointmentFeedbackDTO appointmentFeedbackDTO = new AppointmentFeedbackDTO();
            appointmentFeedbackDTO.setFeedback(appointment.getFeedback());
            appointmentFeedbackDTO.setStarAppointment(appointment.getStarAppointment());
            return ResponseEntity.ok(appointmentFeedbackDTO);
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found appointment");
            logger.error("Not found appointment", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //----------------------------------- UPDATE INFORMATION -----------------------------------
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


}
