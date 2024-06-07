package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.auth.AuthenticationRequest;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.user.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    private AuthenticationResponse register(RegisterRequest request, Role role) {
        return authenticationService.register(request, role);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> customerRegister(
            @RequestBody RegisterRequest request
    ) {
        Role role = Role.CUSTOMER;
        return ResponseEntity.ok(register(request, role));
    }

    @PostMapping("/staffs-register/{role}")
    public ResponseEntity<AuthenticationResponse> dentistRegister(
            @PathVariable Role role,
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(register(request, role));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Log response details
        logger.info("Authentication response: {}", response.getToken());

        return ResponseEntity.ok(response);
    }

}