package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserAppointDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;

import com.example.DentistryManagement.service.AppointmentService;
import com.example.DentistryManagement.service.NotificationService;
import com.example.DentistryManagement.service.UserService;
import com.example.DentistryManagement.service.AuthenticationService;
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

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class StaffController {
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/den")
    public ResponseEntity<Optional<List<UserDTO>>> findDentistManage() {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.STAFF)) {
                String userId = authentication.getName();
                Optional<List<Client>> clientsOptional = userService.findDenByStaff(userId);

                // Kiểm tra nếu danh sách clients không rỗng và tồn tại
                if (clientsOptional.isPresent() && !clientsOptional.get().isEmpty()) {
                    // Chuyển đổi danh sách Client sang danh sách ClientDTO
                    List<UserDTO> clientDTOs = clientsOptional.get().stream()
                            .map(client -> {
                                UserDTO clientDTO = new UserDTO();
                                clientDTO.setFirstName(client.getFirstName());
                                clientDTO.setPhone(client.getPhone());
                                clientDTO.setMail(client.getMail());
                                clientDTO.setLastName(client.getLastName());
                                clientDTO.setBirthday(client.getBirthday());

                                return clientDTO;
                            })
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(Optional.of(clientDTOs));
                } else {
                    // lỗi 403
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
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
    @GetMapping("/cus")
    public ResponseEntity<Optional<List<UserDTO>>> findAllCusManage() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.STAFF)) {
                String userId = authentication.getName();
                Optional<List<Client>> clientsOptional = userService.findCusinClinic(userId);

                // Kiểm tra nếu danh sách clients không rỗng và tồn tại
                if (clientsOptional.isPresent() && !clientsOptional.get().isEmpty()) {

                    List<UserDTO> clientDTOs = clientsOptional.get().stream()
                            .map(client -> {
                                UserDTO clientDTO = new UserDTO();
                                clientDTO.setFirstName(client.getFirstName());
                                clientDTO.setPhone(client.getPhone());
                                clientDTO.setMail(client.getMail());
                                clientDTO.setLastName(client.getLastName());
                                clientDTO.setBirthday(client.getBirthday());
                                return clientDTO;
                            })
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(Optional.of(clientDTOs));
                } else {
                    // lỗi 403
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
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
    @GetMapping("/appointhist")
    public ResponseEntity<Optional<List<Appointment>>> findAllAppointmentManage() {
        try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authenticationService.isUserAuthorized(authentication, "userId", Role.STAFF)) {
            String userId = authentication.getName();
            return ResponseEntity.ok(appointmentService.findApointmentclinic(userId));
        } else {
            //không có quyền, lỗi 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }  } catch (Exception e) {
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
    @GetMapping("/den/{id}")
    public ResponseEntity<?> findAllDenByStaff(@PathVariable("id") String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.STAFF)) {
                UserDTO userDTO =new UserDTO();
                Client client= userService.userInfo(id);
                userDTO.setFirstName(client.getFirstName());
                userDTO.setPhone(client.getPhone());
                userDTO.setMail(client.getMail());
                userDTO.setLastName(client.getLastName());
                userDTO.setBirthday(client.getBirthday());

                Optional<List<Appointment>> appointment=appointmentService.denAppoint(id);
                UserAppointDTO userAppointDTO = new UserAppointDTO();
                userAppointDTO.setUserDTO(userDTO);
                userAppointDTO.setAppointment(appointment);
                return ResponseEntity.ok(userAppointDTO);
            } else {
                //không có quyền, lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }  } catch (Exception e) {
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
    @GetMapping("/cus/{id}")
    public ResponseEntity<?> findAllCusByStaff(@PathVariable("id") String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.STAFF)) {
                UserDTO userDTO =new UserDTO();
                Client client= userService.userInfo(id);
                userDTO.setFirstName(client.getFirstName());
                userDTO.setPhone(client.getPhone());
                userDTO.setMail(client.getMail());
                userDTO.setLastName(client.getLastName());
                userDTO.setBirthday(client.getBirthday());
                String userId = authentication.getName();
                Optional<List<Appointment>> appointment=appointmentService.cusAppoint(id,userId);
                UserAppointDTO userAppointDTO = new UserAppointDTO();
                userAppointDTO.setUserDTO(userDTO);
                userAppointDTO.setAppointment(appointment);
                return ResponseEntity.ok(userAppointDTO);
            } else {
                //không có quyền, lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }  } catch (Exception e) {
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

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.STAFF)) {
                String userid = authentication.getName();
                Optional<List<Notification>> notice = notificationService.reiveNoti(userid) ;

                return ResponseEntity.ok(notice);
            } else {
                //không có quyền, lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }  } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
