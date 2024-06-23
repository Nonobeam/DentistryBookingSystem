
package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.passwordResetToken.PasswordResetToken;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.repository.PasswordResetTokenRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "User API")
public class UserController {

    private final UserService userService;
    private final DentistService dentistService;
    private final DentistScheduleService dentistScheduleService;
    private final NotificationService notificationService;
    private final AppointmentService appointmentService;
    private final PasswordResetTokenService tokenService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ClinicService clinicService;
    private final AppointmentRepository appointmentRepository;
    private final Logger LOGGER = LogManager.getLogger(UserController.class);
    private final ServiceService serviceService;

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
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok(userDTO.getUserDTOFromUser(user));
    }


    //----------------------------------- APPOINTMENT INFORMATION -----------------------------------


    @Operation(summary = "Customer")
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

    @Operation(summary = "All Clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/dependentList")
    public ResponseEntity<List<Dependent>> getAllDependentByCustomer() {
        try {
            String mail = userService.mailExtract();
            List<Dependent> dependentsList = userService.findDependentByCustomer(mail);
            return ResponseEntity.ok(dependentsList);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @GetMapping("/dependentNew")
    public ResponseEntity createDependentByCustomer(@RequestBody Dependent dependent) {
        try {
            String mail = userService.mailExtract();
            dependent.setUser(userService.findClientByMail(mail));
            return ResponseEntity.ok(userService.saveDependent(dependent));
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @GetMapping("/available-service")
    public ResponseEntity<List<Services>> getAvailableServices(
            @RequestParam LocalDate bookDate,
            @RequestParam String clinicId) {

        List<Services> dentistService;
        try {
            Clinic clinic = clinicService.findClinicByID(clinicId);
            dentistService = dentistScheduleService
                    .getServiceNotNullByDate(bookDate, clinic);
            return ResponseEntity.ok(dentistService);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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


    @Operation(summary = "Get All Services By Clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @GetMapping("/all-service/{clinicID}")
    public ResponseEntity<HashMap<String, Services>> getAllServiceByClinic(@RequestParam LocalDate workDate,
                                                                           @PathVariable String clinicID) {
        try {
            HashMap<String, Services> servicesByClinic = new HashMap<>();
            Clinic clinic = clinicService.findClinicByID(clinicID);
            List<DentistSchedule> dentistScheduleList = clinic.getDentistScheduleList();
            for (DentistSchedule dentistSchedule : dentistScheduleList) {
                if (dentistSchedule.getWorkDate().equals(workDate)) {
                    if (dentistSchedule.getAvailable() == 1) {
                        servicesByClinic.put(dentistSchedule.getServices().getServiceID(), dentistSchedule.getServices());
                    }
                }
            }
            return ResponseEntity.ok(servicesByClinic);
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
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedules(
            @RequestParam LocalDate workDate,
            @PathVariable String clinicID,
            @RequestParam String servicesId) {

        Optional<List<DentistSchedule>> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinicID);

        return dentistScheduleList
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }


    @Operation(summary = "Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/booking/{dentistScheduleId}")
    public ResponseEntity<Appointment> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID) {
        try {
            Client client = userService.findClientByMail(userService.mailExtract());
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
                throw new Error("Over booked for today!");
            }

            if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
                throw new Error("Full appointment for this date!");
            }
            Appointment newAppointment = new Appointment();
            newAppointment.setUser(client);
            newAppointment.setServices(dentistSchedule.getServices());
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
        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
    public ResponseEntity<String> deleteBooking(@PathVariable String appointmentId) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            String dentistScheduleId = appointment.getDentistScheduleId();
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            //Check for duplicate cancelled just in case
            if(appointment.getStatus() == 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment has already been cancelled");
            }
            appointment.setStatus(0);
            Optional<List<DentistSchedule>> unavailableSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 0);
            unavailableSchedule.ifPresent(schedules -> {
                schedules.forEach(schedule -> schedule.setAvailable(1));
            });
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment has been cancelled");
        }catch (Error e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Show user Appointment history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("appointment-history")
    public ResponseEntity<List<Appointment>> getAppointmentHistory
            (@RequestParam(required = false) LocalDate workDate,
             @RequestParam(required = false) Integer status) {
        try{
            Client user = userService.findClientByMail(userService.mailExtract());
            List<Appointment> appointmentList = appointmentService.findAppointmentHistory(user, workDate, status);
            return ResponseEntity.ok(appointmentList);
        } catch (Error e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "User update their profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/info/update")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            Client user = userRepository.findByMail(userService.mailExtract()).orElse(null);
            if (user != null) {
                userDTO.getUserDTOFromUser(user);
            }
            return ResponseEntity.ok(userDTO);
        } catch (Error e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "User API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/appointment-history/")
    public ResponseEntity<Optional<List<AppointmentDTO>>> appointmentHistory(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            Optional<List<Appointment>> appointmentList = null;
            if (date != null || (search != null && !search.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByCustomer(date, search, mail);
            } else {
                appointmentList = appointmentService.findAllAppointmentByCustomer(userService.mailExtract());
            }
            List<AppointmentDTO> appointmentDTOList = appointmentList.get().stream()
                    .map(appointmentriu -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentriu.getServices().getName());
                        appointment.setStatus(appointmentriu.getStatus());
                        appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                        if (appointmentriu.getStaff() != null) {
                            if (appointmentriu.getUser() != null) {
                                appointment.setUser(appointmentriu.getUser().getFirstName() + appointmentriu.getUser().getLastName());
                            } else {
                                appointment.setDependent(appointmentriu.getDependent().getFirstName() + appointmentriu.getDependent().getLastName());
                            }
                        } else {
                            if (appointmentriu.getDependent() != null) {
                                appointment.setDependent(appointmentriu.getDependent().getFirstName() + appointmentriu.getDependent().getLastName());
                            } else
                                appointment.setUser(appointmentriu.getUser().getFirstName() + appointmentriu.getUser().getLastName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Optional.of(appointmentDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
