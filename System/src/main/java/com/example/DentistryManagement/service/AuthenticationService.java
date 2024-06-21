
package com.example.DentistryManagement.service;

import com.example.DentistryManagement.DTO.TemporaryUser;
import com.example.DentistryManagement.auth.AuthenticationRequest;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.StaffRepository;
import com.example.DentistryManagement.repository.TemporaryUserRepository;
import com.example.DentistryManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${spring.confirmation.link.baseurl}")
    private String confirmationLinkBaseUrl;

    private final JwtService jwtService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final DentistRepository dentistRepository;
    private final TemporaryUserRepository temporaryUserRepository;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    public AuthenticationResponse registerStaff(RegisterRequest request, Clinic clinic) {
        if (userRepository.existsByPhoneOrMailAndStatus(request.getPhone(), request.getMail(), 1)) {
            throw new Error("Phone or mail is already existed");
        }

        Client user;
        try {
            user = Client.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
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

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse registerDentist(RegisterRequest request, Clinic clinic, Staff staff) {
        if (userRepository.existsByPhoneOrMailAndStatus(request.getMail(), request.getPhone(), 1)) {
            throw new Error("Phone or mail is already existed");
        }

        Client user;
        try {
            user = Client.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
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
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())  // Include lastName
                    .phone(request.getPhone())
                    .mail(request.getMail())  // Include mail
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
        mailService.sendSimpleMessage(email, subject, text);
    }

    @Transactional
    public String confirmUser(String token) {
        TemporaryUser temporaryUser = temporaryUserRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new Error("Invalid confirmation token"));

        if (temporaryUser == null) {
            return "Didn't find user with token: " + token;
        }

        Client user = Client.builder()
                .firstName(temporaryUser.getFirstName())
                .lastName(temporaryUser.getLastName())
                .phone(temporaryUser.getPhone())
                .mail(temporaryUser.getMail())
                .password(temporaryUser.getPassword())
                .role(temporaryUser.getRole())
                .birthday(temporaryUser.getBirthday())
                .status(1)
                .build();

        userRepository.save(user);
        temporaryUserRepository.delete(temporaryUser);

        var jwtToken = jwtService.generateToken(user);

        return jwtToken;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getMail(),
                        request.getPassword()
                )
        );
        Client user = null;
        try {
            user = userRepository.findByMail(request.getMail())
                    .orElseThrow();
        } catch (Exception e) {
            logger.error(e.toString());

            if (user == null) {
                throw new Error("Cannot find the user with mail" + request.getMail());
            } else {
                throw new Error("Some unexpected problem has been happened");
            }
        }

        System.out.println("User" + user);
        var jwtToken = jwtService.generateToken(user);
        logger.info("Token in service: {}", jwtToken);

        if (jwtToken == null) {
            System.out.printf("Token is null");
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

    public boolean isUserAuthorized(Authentication authentication, String userId, Role userRole) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String authenticatedUserId = authentication.getName();
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                boolean hasUserRole = authorities.stream()
                        .anyMatch(authority -> authority.getAuthority().equals(userRole.name()));

                boolean isUserIdMatched = authenticatedUserId.equals(userId);
                return hasUserRole && isUserIdMatched;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
