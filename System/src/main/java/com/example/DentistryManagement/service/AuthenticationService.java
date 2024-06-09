
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request, Role role) {
        var player = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .mail(request.getMail())
                .name(request.getLastName() + " " + request.getFirstName())
                .role(role)
                .status(1)
                .build();
        userRepository.save(player);
        var jwtToken = jwtService.generateToken(player);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

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
    public boolean isUserAuthorized(Authentication authentication, String userId, Role userRole) {
        if (authentication != null && authentication.isAuthenticated()) {

            String authenticatedUserId = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


            boolean hasUserRole = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals(userRole.name()));

            boolean isUserIdMatched = authenticatedUserId.equals(userId);
            // Trả về true nếu vai trò và ID của người dùng khớp với yêu cầu
            return hasUserRole && isUserIdMatched;
        }
        // Trả về false nếu người dùng chưa xác thực
        return false;
    }

}
