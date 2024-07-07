package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.DashboardBoss;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.AppointmentService;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.ServiceService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/boss")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Boss API")
public class BossController {
    private final UserService userService;
    private final UserMapping userMapping;
    private final ServiceService serviceService;
    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    //----------------------------------- USER ACCOUNT -----------------------------------

    @Operation(summary = "Boss information")
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
            userService.findByMail(userService.mailExtract()).ifPresent(userMapping::getUserDTOFromUser);
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


    @Operation(summary = "Edit manager")
    @PutMapping("/edit/{userID}")
    public ResponseEntity<?> editUser(@PathVariable String userID, @RequestBody UserDTO userDTO) {
        if (userService.isPresentUser(userID).isPresent()) {
            Client updatedUser = userService.findUserById(userID);
            userService.updateUser(userDTO, updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            ErrorResponseDTO error = new ErrorResponseDTO("403", "User could not be update");
            logger.error("User could not be update");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

        }
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {

            Optional<Client> optionalClient = userService.isPresentUser(id);
            if (optionalClient.isPresent()) {
                Client client = optionalClient.get();
                userService.updateUserStatus(client, 0);
                return ResponseEntity.ok("Delete user successfully");
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
                logger.error("User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }


        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //--------------------------- MANAGE MANAGER ---------------------------

    @Operation(summary = "Register a new manager member")
    @PostMapping("/register/manager")
    public ResponseEntity<AuthenticationResponse> registerManager(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.registerManager(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }


    @Operation(summary = "All Managers")
    @GetMapping("/all-manager")
    public ResponseEntity<?> getAllManager() {
        try {
            List<Client> allManager = userService.findAllManager();
            if (allManager != null && !allManager.isEmpty()) {
                List<UserDTO> clientDTOs = allManager.stream()
                        .map(userMapping::getUserDTOFromUser)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            }
            return ResponseEntity.ok("Not found any manager");
        } catch (Error error) {
            throw new Error("Error while getting manager " + error);
        }
    }


    @Operation(summary = "All Managers")
    @GetMapping("/delete-manager")
    public ResponseEntity<?> deleteManager(@RequestParam String managerId) {
        try {
            // Find the manager by id then update the status ---> 0
            Client manager = userService.findUserById(managerId);
            userService.updateUserStatus(manager, 0);
            return ResponseEntity.ok("Delete successfully");
        } catch (Error error) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            return ResponseEntity.status(400).body(errorResponseDTO);
        }
    }


    //--------------------------- MANAGE CLINIC ---------------------------


    @Operation(summary = "Add new service")
    @PostMapping("/service/add")
    public ResponseEntity<?> addNewService(@RequestBody Services services) {
        try {
            Services newService = serviceService.save(services);
            return ResponseEntity.ok(newService);
        } catch (Error error) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorResponseDTO);
        }
    }


    @Operation(summary = "Show all services")
    @PostMapping("/service/all")
    public ResponseEntity<?> showAllServices() {
        try {
            List<Services> services = serviceService.getAll();
            return ResponseEntity.ok(services);
        } catch (Error error) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorResponseDTO);
        }
    }

    // Hard delete due to the little size of table
    @Operation(summary = "Delete service")
    @PostMapping("/service/delete")
    public ResponseEntity<?> deleteService(@RequestBody String servicesId) {
        try {
            serviceService.deleteServiceById(servicesId);
            return ResponseEntity.ok("Delete successfully");
        } catch (Error error) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorResponseDTO);
        }
    }


    @Operation(summary = "Boss dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam Integer year) {
        try {
            Client boss = userService.findClientByMail(userService.mailExtract());
            if (boss == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (date == null) date = LocalDate.now();
            if (year == null) year = LocalDate.now().getYear();
            Map<String, List<Appointment>> dailyAppointments = appointmentService.getDailyAppointmentsByClinic(date);
            Map<String, Map<Integer, Long>> yearlyAppointments = appointmentService.getAppointmentsByClinicsForYear(year);
            int totalAppointmentInMonth = appointmentService.totalAppointmentsInMonthByBoss();
            int totalAppointmentInYear = appointmentService.totalAppointmentsInYearByBoss();

            DashboardBoss dashboardResponse = new DashboardBoss(dailyAppointments, yearlyAppointments, totalAppointmentInMonth, totalAppointmentInYear);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found data in dashboard");
            logger.error("Not found data in dashboard");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }
}
