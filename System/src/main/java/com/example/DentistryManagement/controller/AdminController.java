package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class AdminController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Clinic user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/denlist")
    public ResponseEntity<Optional<List<Client>>> denlist() {
        try {
            Optional<List<Client>> clients = userService.findAllDen();
            if (clients.isPresent() && clients.get().isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }


}
