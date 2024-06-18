package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.core.dentistry.Services;
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

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Staff API")
public class StaffController {
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final DentistScheduleService dentistScheduleService;
    private final ServiceService serviceService;
    private final DentistService dentistService;
    private final StaffService staffService;


//    @Operation(summary = "All Services in System")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Error")
//
//    })
//    @GetMapping("/all-services")
//    public ResponseEntity<List<Services>> getAllServices() {
//        try {
//            return ResponseEntity.ok(serviceService.findAllServices());
//        } catch (Error error) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

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


    @Operation(summary = "Set Dentist Schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set the schedule"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/set-schedule")
    public ResponseEntity<?> setDentistSchedule(@RequestParam String dentistID,
                                                @RequestParam LocalDate startDate,
                                                @RequestParam LocalDate endDate,
                                                @RequestParam String timeSlotID,
                                                @RequestParam String clinicID,
                                                @RequestParam String serviceID) {
        try {
            dentistScheduleService.setDentistSchedule(dentistID, startDate, endDate, timeSlotID, clinicID, serviceID);
            return ResponseEntity.ok("Schedule set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @Operation(summary = "Delete Dentist Schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the schedule"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete-schedule")
    public ResponseEntity<?> deleteDentistSchedule(@RequestParam String dentistID,
                                                   @RequestParam LocalDate workDate) {
        try {
            dentistScheduleService.deleteDentistSchedule(dentistID, workDate);
            return ResponseEntity.ok("Schedule deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}