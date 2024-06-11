package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.ClinicService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/manager")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class ManagerController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ClinicService clinicService;
    @Operation(summary = "Clinic user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/denlist")
    public ResponseEntity<Optional<List<Client>>> denList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.MANAGER)) {
                String userId = authentication.getName();
                Optional<List<Client>> clients = userService.findAllDenByClinic(userId);
                if (clients.isPresent() && clients.get().isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(clients);
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }
    @Operation(summary = "Clinic user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/stafflist")
    public ResponseEntity<Optional<List<Client>>> staffList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.MANAGER)) {
                String userId = authentication.getName();
                Optional<List<Client>> clients = userService.findAllStaffByClinic(userId);
                if (clients.isPresent() && clients.get().isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(clients);
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }

    @Operation(summary = "Clinic ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/cliniclist")
    public ResponseEntity<Optional<List<Clinic>>> clinicList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.MANAGER)) {
                String userId = authentication.getName();
                Optional<List<Clinic>> clinics = clinicService.findClinicByManager(userId);
                if (clinics.isPresent() && clinics.get().isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(clinics);
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }

}
