
package com.example.DentistryManagement.service;

import com.example.DentistryManagement.auth.AuthenticationRequest;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request, Role role) {

        if (userRepository.existsByPhoneOrMail(request.getPhone(), request.getMail())) {
            throw new Error("Phone or mail is already existed");
        }

        Client user;
        try {
            user = Client.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .mail(request.getMail())
                    .name(request.getLastName() + " " + request.getFirstName())
                    .role(role)
                    .status(1)
                    .build();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new Error("Some thing when wrong while creating a new user, please check your input field");
        }

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
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
        var player = userRepository.findByName(request.getMail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(player);

        logger.info("Token in service: " + jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
