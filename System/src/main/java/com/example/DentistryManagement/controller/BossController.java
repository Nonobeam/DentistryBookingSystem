package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.DashboardBoss;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.AppointmentService.AppointmentAnalyticService;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.ServiceService;
import com.example.DentistryManagement.service.UserService.UserManagerService;
import com.example.DentistryManagement.service.UserService.UserService;
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
import java.util.stream.Collectors;

@RequestMapping("/api/v1/boss")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Boss API")
public class BossController {
    private final UserService userService;
    private final UserManagerService managerService;
    private final UserMapping userMapping;
    private final ServiceService serviceService;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AppointmentAnalyticService appointmentAnalyticService;


    //----------------------------------- USER ACCOUNT -----------------------------------

    @Operation(summary = "Boss information")
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


    //--------------------------- MANAGE MANAGER ---------------------------

    @Operation(summary = "Register a new manager member")
    @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.registerManager(request);
            return ResponseEntity.ok(response);
        } catch (Error e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    @Operation(summary = "All Managers")
    @GetMapping("/all-manager")
    public ResponseEntity<?> getAllManager() {
        try {
            List<Client> allManager = managerService.findAllManager();
            if (allManager != null && !allManager.isEmpty()) {
                List<UserDTO> managerList = allManager.stream()
                        .map(userMapping::getUserDTOFromUser)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(managerList);
            }
            return ResponseEntity.ok("Not found any manager");
        } catch (Error error) {
            throw new Error("Error while getting manager " + error);
        }
    }


    @Operation(summary = "Delete a manager")
    @DeleteMapping("/delete-manager/{managerID}")
    public ResponseEntity<?> deleteManager(@PathVariable("managerID") String managerID) {
        try {
            // Find the manager by id then update the status ---> 0
            Client removeManager = userService.findUserById(managerID);
            userService.updateUserStatus(removeManager, 0);
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
    @GetMapping("/service/all")
    public ResponseEntity<?> showAllServices() {
        try {
            List<Services> servicesList = serviceService.getAll();
            return ResponseEntity.ok(servicesList);
        } catch (Error error) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorResponseDTO);
        }
    }

    // Hard delete due to the little size of table
    @Operation(summary = "Delete service")
    @DeleteMapping("/service/delete/{serviceID}")
    public ResponseEntity<?> deleteService(@PathVariable String serviceID) {
        try {
            serviceService.deleteServiceById(serviceID);
            return ResponseEntity.ok("Delete successfully");
        } catch (Error error) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorResponseDTO);
        }
    }


    @Operation(summary = "Boss dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(required = false) Integer year) {
        try {
            Client boss = userService.findUserByMail(userService.mailExtract());
            if (boss == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (date == null) date = LocalDate.now();
            if (year == null) year = LocalDate.now().getYear();
            Map<String, List<Appointment>> dailyAppointments = appointmentAnalyticService.getAppointmentsByDate(date);
            Map<String, Map<Integer, Long>> yearlyAppointments = appointmentAnalyticService.getAppointmentsByYear(year);
            int totalAppointmentInMonth = appointmentAnalyticService.totalAppointmentsInMonth();
            int totalAppointmentInYear = appointmentAnalyticService.totalAppointmentsInYear();

            DashboardBoss dashboardResponse = new DashboardBoss(dailyAppointments, yearlyAppointments, totalAppointmentInMonth, totalAppointmentInYear, null);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found data in dashboard");
            logger.error("Not found data in dashboard");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }
}
