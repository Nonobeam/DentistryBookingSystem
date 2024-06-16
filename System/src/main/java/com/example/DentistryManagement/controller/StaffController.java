package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.mail.Notification;

import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class StaffController {
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final ServiceService serviceService;
    private final DentistService dentistService;
    private final StaffService staffService;


    @Operation(summary = "All Services in System")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/all-services")
    public ResponseEntity<List<Services>> getAllServices() {
        try {
            return ResponseEntity.ok(serviceService.findAllServices());
        } catch (Error error) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "All Services in System")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/set-service/{dentistID}")
    public ResponseEntity<Dentist> updateDentistService(@PathVariable String dentistID, @RequestParam String serviceID) {
        Dentist dentist;
        Services service;
        try {
            dentist = dentistService.findDentistByID(dentistID);
            service = serviceService.findServiceByID(serviceID);
            dentist.getServicesList().add(service);

            return ResponseEntity.ok(dentist);
        } catch (Error error) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "All Dentists manage by a Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/dentist/all")
    public ResponseEntity<List<Dentist>> getAllDentists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();

        Staff staff;
        List<Dentist> dentists;
        try {
            staff = staffService.findStaffByMail(mail);
            dentists = dentistService.findDentistByStaff(staff);
            return ResponseEntity.ok(dentists);
        } catch (Error error) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/appointment-history")
    public ResponseEntity<Optional<List<Appointment>>> findAllAppointmentHistory() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String mail = authentication.getName();
            return ResponseEntity.ok(appointmentService.findApointmentclinic(mail));
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping()
    public ResponseEntity<?> receiveNotification() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String mail = authentication.getName();
            Optional<List<Notification>> notice = notificationService.receiveNotice(mail);

            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}