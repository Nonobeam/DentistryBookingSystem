package com.example.DentistryManagement.auth;

import com.example.DentistryManagement.config.JwtService;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.User;
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

//    public AuthenticationResponse register(RegisterRequest request) {
//        var player = User.builder()
//                .name(request.getName())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role)
//                .build();
//        userRepository.save(player);
//        var jwtToken = jwtService.generateToken(player);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );
        var player = userRepository.findByName(request.getName())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(player);

        logger.info("Token in service: " + jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
