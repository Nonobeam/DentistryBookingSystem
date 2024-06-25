package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
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

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Admin API")
public class AdminController {
    private final UserService userService;
    private final UserMapping userMapping;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Operation(summary = "Admin")
    @GetMapping("/dentistList")
    public ResponseEntity<?> dentistList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findDentistInClinic(search);

            } else {
                userList = userService.findAllDentist();
            }
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Null data make mistake");
            logger.error("Null data make mistake");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/managerList")
    public ResponseEntity<?> managerList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.searchManager(search);
            } else {
                userList = userService.findAllManager();
            }
            return ResponseEntity.ok(userList);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Data make mistake");
            logger.error("Data make mistake");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


    @Operation(summary = "Admin")

    @GetMapping("/staffList")
    public ResponseEntity<?> staffList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findStaffInClinic(search);
            } else {
                userList = userService.findAllStaff();
            }
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Data make mistake");
            logger.error("Data make mistake");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/customerList")
    public ResponseEntity<?> customerList(@RequestParam(required = false) String search) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.searchCustomer(search);
            } else {
                userList = userService.findAllCustomer();
            }
            return ResponseEntity.ok(userList);

        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Data make mistake");
            logger.error("Data make mistake");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Admin")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody UserDTO updatedUser) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        try {
            if (userService.isPresentUser(id).isPresent()) {
                Client client = userMapping.mapUser(updatedUser);
                userService.updateUser(client);
                return ResponseEntity.ok(client);
            } else {
                error.setCode("403");
                error.setMessage("User could not be update.");
                logger.error("User could not be update.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

            }

        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("An error occurred while creating the user.");
            logger.error("An error occurred while creating the user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        try {

            if (userService.isPresentUser(id).isPresent()) {
                Optional<Client> c = userService.isPresentUser(id);
                if (c.isPresent()) {
                    Client client = c.get();
                    userService.updateUserStatus(client, 0);
                    return ResponseEntity.ok().build();
                } else {
                    error.setCode("204");
                    error.setMessage("Not found any client");
                    logger.error("Not found any client");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }

            } else {
                error.setCode("204");
                error.setMessage("User could not be delete.");
                logger.error("User could not be delete.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }

        } catch (Exception e) {
            error.setCode("400");
            error.setMessage("Data make mistake");
            logger.error("Data make mistake");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
