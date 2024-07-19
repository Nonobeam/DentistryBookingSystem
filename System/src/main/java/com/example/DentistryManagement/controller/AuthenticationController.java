package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.auth.AuthenticationRequest;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.UserService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;
    private final UserService userService;


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
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Error e) {
            return ResponseEntity.status(403).body(new ErrorResponseDTO("403", "Cannot find user"));
        }
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    //--------------------------------FORGOT PASSWORD----------------------------------
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String mail) {
        try {
            Client user = userService.findUserByMail(mail);
            if (user != null) {
                String token = UUID.randomUUID().toString();
                authenticationService.createPasswordResetTokenForUser(user, token);
                authenticationService.sendPasswordResetEmail(user.getMail(), token);
                return ResponseEntity.ok("Password reset link has been sent to your email");
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PostMapping("/resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestParam("password") String password) {
        try {
            String validationResult = authenticationService.validatePasswordResetToken(token);
            if (validationResult.equalsIgnoreCase("invalid")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }
            authenticationService.resetPassword(token, password);
            return ResponseEntity.ok("Password has been reset successfully");
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found user");
            logger.error("Not found user", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}