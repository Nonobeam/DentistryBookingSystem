package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.DashboardBoss;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.repository.ServiceRepository;
import com.example.DentistryManagement.service.AppointmentService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RequestMapping("/api/v1/boss")
@RestController
@RequiredArgsConstructor
@Tag(name = "Boss API")
public class BossController {

    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Operation(summary = "Add new service")
    @PostMapping("/service/add")
    public ResponseEntity<Services> addNewService(@RequestBody Services services) {
        return ResponseEntity.ok(serviceRepository.save(services));
    }


    @Operation(summary = "Boss dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("year") int year) {
        try {
            Client boss = userService.findClientByMail(userService.mailExtract());
            if (boss == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<Clinic, List<Appointment>> dailyAppointments = appointmentService.getDailyAppointmentsByClinic(date);
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
