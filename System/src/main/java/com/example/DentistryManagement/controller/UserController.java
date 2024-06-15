
package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.passwordResetToken.PasswordResetToken;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.repository.PasswordResetTokenRepository;
import com.example.DentistryManagement.repository.UserRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Operation(summary = "All users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Client>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Operation(summary = "Find a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/all/{userID}")
    public ResponseEntity<Client> findUser(@PathVariable String userID) {
        return ResponseEntity.ok(userService.findUserByID(userID));
    }

//    @Operation(summary = "All dentists follow status")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found")
//    })
//    @GetMapping("/allDentist/{status}")
//    public ResponseEntity<List<Client>> findAllDentistsByStatus(@PathVariable int status, @PathVariable Role role) {
//        return ResponseEntity.ok(dentistService.findAllDentistsByStatus(status, role));
//    }

//    @Operation(summary = "Get schedule for a dentist")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found")
//    })
//    @GetMapping("/dentist/schedule/{dentistID}")
//    public ResponseEntity<Schedule> findAllDentistsByStatus(@PathVariable UUID dentistID, @Param("date")LocalDate date) {
//        return ResponseEntity.ok(dentistService.getDentistSchedule(dentistID, date));
//    }

    @PostMapping("/sendMail/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody Notification notificationStructure) {
        notificationService.sendMail(mail, notificationStructure);
        return "Successfully";
    }


    @Operation(summary = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{status}")
    public ResponseEntity<Appointment> setAppointmentStatus(@PathVariable("status") int status, Appointment appointment) {

        try {
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    @GetMapping("/available-service")
//    public ResponseEntity<List<Service>> getAvailableServices(
//            @RequestParam LocalDate bookDate,
//            @RequestParam Clinic clinic) {
//
//        Optional<List<Service>> dentistService = dentistScheduleService
//                .getServiceNotFull(bookDate, clinic);
//
//        return dentistService
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.noContent().build());
//    }

    @GetMapping("/available-schedules")
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedules(
            @RequestParam LocalDate bookDate,
            @RequestParam Clinic clinic,
            @RequestParam Service service) {

        Optional<List<DentistSchedule>> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(bookDate, service, 1, clinic);

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
    @PostMapping("/make-booking")
    public ResponseEntity<Appointment> makeBooking( @RequestBody DentistSchedule dentistSchedule) {
        try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail= authentication.getName();
        Client client = userService.findClientByMail(mail);
        if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
            throw new Error("Over booking in today!");
        }

        if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
            throw new Error("Over appointment in this date!");
        }
        Appointment newAppointment = new Appointment();
        newAppointment.setUser(client);
        newAppointment.setService(dentistSchedule.getService());
        newAppointment.setClinic(dentistSchedule.getClinic());
        newAppointment.setDate(dentistSchedule.getWorkDate());
        newAppointment.setTimeSlot(dentistSchedule.getTimeslot());

        return ResponseEntity.ok(newAppointment);
        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("mail") String mail) {
        Client user = userRepository.findByMail(mail).orElse(null);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            tokenService.createPasswordResetTokenForUser(user, token);
            tokenService.sendPasswordResetEmail(mail, token);
        }
        return ResponseEntity.ok("Password reset link has been sent to your email");
    }

    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestParam("password") String password) {
        String validationResult = tokenService.validatePasswordResetToken(token);
        if (validationResult != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        Client user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        tokenRepository.delete(passToken); // Invalidate the used token

        return ResponseEntity.ok("Password has been reset successfully");
    }



}
