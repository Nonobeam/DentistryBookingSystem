package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.auth.AuthenticationRequest;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;


    //----------------------------------- REGISTER -----------------------------------


    private String register(RegisterRequest request, Role role) {
        return authenticationService.register(request, role);
    }


    @PostMapping("/register")
    public ResponseEntity<?> customerRegister(
            @RequestBody RegisterRequest request
    ) {
        Role role = Role.CUSTOMER;
        return ResponseEntity.ok(register(request, role));
    }


    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam("token") String token) {
        String jwtToken = authenticationService.confirmUser(token);
        return ResponseEntity.ok(jwtToken);
    }


    //----------------------------------- LOGIN -----------------------------------


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        if (response == null || response.getToken() == null) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("403");
            error.setMessage("Wrong mail or password");
            logger.error("Wrong mail or password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}