
package com.example.DentistryManagement.service;

import com.example.DentistryManagement.DTO.TemporaryUser;
import com.example.DentistryManagement.auth.AuthenticationRequest;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.token.Token;
import com.example.DentistryManagement.core.token.TokenType;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final NotificationService notificationService;
    @Value("${spring.confirmation.link.baseurl}")
    private String confirmationLinkBaseUrl;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final DentistRepository dentistRepository;
    private final AuthenticationManager authenticationManager;
    private final TemporaryUserRepository temporaryUserRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    private void saveUserToken(Client user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Client user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserID());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


//----------------------------------- REGISTER -----------------------------------

    public AuthenticationResponse registerStaff(RegisterRequest request, Clinic clinic) {
        if (userRepository.existsByPhoneOrMailAndStatus(request.getPhone(), request.getMail(), 1)) {
            throw new Error("Phone or mail is already existed");
        }

        Client user;
        try {
            user = Client.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .mail(request.getMail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.STAFF)
                    .birthday(request.getBirthday())
                    .status(1)
                    .build();
        } catch (Exception e) {
            throw new Error("Something went wrong while creating a new user, please check your input field");
        }

        userRepository.save(user);

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setClinic(clinic);
        staffRepository.save(staff);

        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse registerDentist(RegisterRequest request, Clinic clinic, Staff staff) {
        if (userRepository.existsByPhoneOrMailAndStatus(request.getPhone(), request.getMail(), 1)) {
            throw new Error("Phone or mail is already existed");
        }

        Client user;
        try {
            user = Client.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .mail(request.getMail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.DENTIST)
                    .birthday(request.getBirthday())
                    .status(1)
                    .build();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new Error("Something went wrong while creating a new user, please check your input field");
        }

        userRepository.save(user);

        Dentist dentist = new Dentist();
        dentist.setUser(user);
        dentist.setClinic(clinic);
        dentist.setStaff(staff);
        dentistRepository.save(dentist);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerManager(RegisterRequest request) {
        if (userRepository.existsByPhoneOrMailAndStatus(request.getMail(), request.getPhone(), 1)) {
            throw new Error("Phone or mail is already existed");
        }

        Client user;
        try {
            user = Client.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .mail(request.getMail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.MANAGER)
                    .birthday(request.getBirthday())
                    .status(1)
                    .build();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new Error("Something went wrong while creating a new user, please check your input field");
        }

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public String register(RegisterRequest request, Role role) {

        if (userRepository.existsByPhoneOrMailAndStatus(request.getPhone(), request.getMail(), 1)) {
            throw new Error("Phone or mail is already existed");
        }

        String confirmationToken = UUID.randomUUID().toString();
        String confirmationLink = confirmationLinkBaseUrl + "/api/v1/auth/confirm?token=" + confirmationToken;

        sendConfirmationEmail(request.getMail(), confirmationLink);

        // Save a temporary user or a confirmation token to track the confirmation
        TemporaryUser temporaryUser;
        try {
            temporaryUser = TemporaryUser.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .mail(request.getMail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .birthday(request.getBirthday())
                    .confirmationToken(confirmationToken)
                    .build();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new Error("Something went wrong while creating a new user, please check your input field");
        }

        if (temporaryUser == null) {
            logger.error("ERROR CREATING");
            throw new Error("Something went wrong while creating a new user, please check your input field");
        }

        logger.info("Attempting to save temporary user: {}", temporaryUser);
        temporaryUserRepository.save(temporaryUser);
        logger.info("Temporary user saved successfully");

        return "Confirmation email sent. Please check your email to complete registration.";
    }

    private void sendConfirmationEmail(String email, String confirmationLink) {
        String subject = "Confirm your email";
        String text = "Please click the following link to confirm your email address: " + confirmationLink;
        notificationService.sendSimpleMessage(email, subject, text);
    }

    @Transactional
    public String confirmUser(String token) {
        TemporaryUser temporaryUser = temporaryUserRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new Error("Invalid confirmation token"));

        if (temporaryUser == null) {
            return "Didn't find user with token: " + token;
        }

        Client user = Client.builder()
                .name(temporaryUser.getName())
                .phone(temporaryUser.getPhone())
                .mail(temporaryUser.getMail())
                .password(temporaryUser.getPassword())
                .role(temporaryUser.getRole())
                .birthday(temporaryUser.getBirthday())
                .status(1)
                .build();

        userRepository.save(user);
        temporaryUserRepository.delete(temporaryUser);

        return jwtService.generateToken(user);
    }

    //----------------------------------- LOGIN -----------------------------------


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getMail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new Error("Cannot find user");
        }

        Client user = userRepository.findByMail(request.getMail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        logger.info("Token in service: {}", jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userMail;

        refreshToken = authHeader.substring(7);
        userMail = jwtService.extractMail(refreshToken);
        if (userMail != null) {
            var user = userRepository.findByMail(userMail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


}
