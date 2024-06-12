package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserAppoint;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.AppointmentService;
import com.example.DentistryManagement.service.DentistService;
import com.example.DentistryManagement.service.NotificationService;
import com.example.DentistryManagement.service.UserService;
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
import java.util.stream.Collectors;

@RequestMapping("/api/v1/dentist")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class DentistController {
    private final DentistService dentistService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/appointmenttoday")
    public ResponseEntity<Optional<List<AppointmentDTO>>> appointmentList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String id= authentication.getName();
            Optional<List<Appointment>> appointlist = appointmentService.findAppointByDen(id);
            if (appointlist.isPresent() && !appointlist.get().isEmpty()) {
                // Chuyển đổi danh sách Client sang danh sách ClientDTO
                List<AppointmentDTO> applist = appointlist.get().stream()
                        .map(appointmentriu -> {
                            AppointmentDTO appointment = new AppointmentDTO();
                            appointment.setService(appointmentriu.getService());
                            appointment.setStatus(appointmentriu.getStatus());
                            appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                            if (appointmentriu.getStaff()!=null){
                                if(appointmentriu.getUser() != null){
                                    appointment.setPatient(appointmentriu.getUser().getFirstName());
                                } else {
                                    appointment.setPatient(appointmentriu.getDependent().getFirstName());
                                }
                            }else {
                                if(appointmentriu.getDependent()!=null){
                                    appointment.setPatient(appointmentriu.getDependent().getFirstName());
                                }else appointment.setPatient(appointmentriu.getUser().getFirstName());
                            }

                            return appointment;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(Optional.of(applist));
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }
    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/reminder")
    public ResponseEntity<Notification> reminderNoti(Notification notification) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String id= authentication.getName();
            if (notification!=null){
                Notification insertedNotification = notificationService.insertNotification(notification);

                return ResponseEntity.ok(insertedNotification);
            }else {
                // lỗi 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/appointlist")
    public ResponseEntity<Optional<List<Appointment>>> appointmentHistory() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String id= authentication.getName();
            Optional<List<Appointment>> appointmentlist = appointmentService.findAllAppointByDen(id);
            return ResponseEntity.ok(appointmentlist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @Operation(summary = "Dentist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/cus/{id}")
    public ResponseEntity<?> findAllCusByStaff(@PathVariable("id") String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String userid = authentication.getName();
                UserDTO userDTO =new UserDTO();
                Client client= userService.userInfo(id);
                userDTO.setFirstName(client.getFirstName());
                userDTO.setPhone(client.getPhone());
                userDTO.setMail(client.getMail());
                userDTO.setLastName(client.getLastName());
                userDTO.setBirthday(client.getBirthday());
                String userId = authentication.getName();
                Optional<List<Appointment>> appointment=appointmentService.cusAppointfollowden(id,userId);
                UserAppoint userAppoint= new UserAppoint();
                userAppoint.setUserDTO(userDTO);
                userAppoint.setAppointment(appointment);
                return ResponseEntity.ok(userAppoint);
             } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
