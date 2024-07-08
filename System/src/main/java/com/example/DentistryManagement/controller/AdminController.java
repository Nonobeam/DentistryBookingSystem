package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.DentistResponseDTO;
import com.example.DentistryManagement.DTO.StaffResponseDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Admin API")
public class AdminController {
    private final UserService userService;
    private final UserMapping userMapping;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);



    @Operation(summary = "Register a new boss")
    @PostMapping("/register/boss")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.registerBoss(request);
            return ResponseEntity.ok(response);
        } catch (Error e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/dentistList")
    public ResponseEntity<?> dentistList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findDentistFollowSearching(search);

            } else {
                userList = userService.findAllDentist();
            }
            List<DentistResponseDTO> dentistList = userList.stream()
                    .map(userMapping::convertToDentistDTO)
                    .collect(Collectors.toList());
            if (!dentistList.isEmpty()) {
                return ResponseEntity.ok(dentistList);
            } else return ResponseEntity.ok("Not found any dentist user  ");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/managerList")
    public ResponseEntity<?> managerList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findManagerFollowSearching(search);
            } else {
                userList = userService.findAllManager();
            }
            List<UserDTO> managerList = userList.stream()
                    .map(userMapping::getUserDTOFromUser)
                    .collect(Collectors.toList());
            if (!managerList.isEmpty()) {
                return ResponseEntity.ok(managerList);
            } else
                return ResponseEntity.ok("Not found any manager user  ");

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Admin")

    @GetMapping("/staffList")
    public ResponseEntity<?> staffList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findStaffFollowSearching(search);
            } else {
                userList = userService.findAllStaff();
            }
            List<StaffResponseDTO> staffDTOList = userList.stream()
                    .map(userMapping::convertToStaffDTO)
                    .collect(Collectors.toList());
            if (!staffDTOList.isEmpty()) {
                return ResponseEntity.ok(staffDTOList);
            } else return ResponseEntity.ok("Not found any staff user ");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Staff user not found");
            logger.error("Staff user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/customerList")
    public ResponseEntity<?> customerList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findCustomerFollowSearching(search);
            } else {
                userList = userService.findAllCustomer();
            }
            List<UserDTO> userDTOList = userList.stream()
                    .map(userMapping::getUserDTOFromUser)
                    .collect(Collectors.toList());
            if (!userDTOList.isEmpty()) {
                return ResponseEntity.ok(userDTOList);
            } else return ResponseEntity.ok("Not found any customer user ");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Admin")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody UserDTO updatedUser) {
        try {
            if (userService.isPresentUser(id).isPresent()) {
                Client client = userService.findUserById(id);
                userService.updateUser(updatedUser, client);
                return ResponseEntity.ok(client);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("403", "User could not be update");
                logger.error("User could not be update");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

            }

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {

            if (userService.isPresentUser(id).isPresent()) {
                Optional<Client> c = userService.isPresentUser(id);
                if (c.isPresent()) {
                    Client client = c.get();
                    userService.updateUserStatus(client, 0);
                    return ResponseEntity.ok("Delete user successfully");
                } else {
                    ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
                    logger.error("User not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                }

            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("403", "User could not be deleted");
                logger.error("User could not be deleted");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
