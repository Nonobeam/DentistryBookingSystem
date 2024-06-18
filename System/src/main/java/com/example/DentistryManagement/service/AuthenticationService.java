
package com.example.DentistryManagement.service;

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
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final DentistRepository dentistRepository;
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
        if (userRepository.existsByPhoneOrMail(request.getPhone(), request.getMail())) {
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


    public AuthenticationResponse register(RegisterRequest request, Role role) {

        if (userRepository.existsByPhoneOrMailAndStatus(request.getPhone(), request.getMail(),1)) {
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
                    .role(role)
                    .birthday(request.getBirthday())
                    .status(1)
                    .build();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new Error("Some thing when wrong while creating a new user, please check your input field");
        }
        var jwtToken = jwtService.generateToken(user);

        userRepository.save(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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
            // Xử lý ngoại lệ
            e.printStackTrace();
            return false;
        }
    }
}
