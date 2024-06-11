package com.example.DentistryManagement.config;

import com.example.DentistryManagement.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
// OncePerRequestFilter: run each times request arrive
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the Authorization header from the HTTP request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String mail;

        // Check if the Authorization header is missing or doesn't start with "Bearer"
        // If so, pass the request and response to the next filter in the chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        jwt = authHeader.substring(7); // Remove "Bearer " from the beginning

        // Extract the username from the JWT token using a service
        mail = jwtService.extractMail(jwt);

        System.out.println("JWT extracted: " + jwt);
        System.out.println("Mail extracted: " + mail);

        // If the username is not null and there is no authentication in the current SecurityContext
        if (mail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Get UserDetails from the database using the username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(mail);

            System.out.println("User details loaded: " + userDetails.getUsername());
            System.out.println("Authorities: " + userDetails.getAuthorities());

            // Check if the JWT token is valid for the UserDetails
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an Authentication token for the user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No password is needed for JWT authentication
                        userDetails.getAuthorities()
                );
                // Set additional details for the authentication token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Set the authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication successful for user " + mail + " " + userDetails.getAuthorities());
            } else {
                System.out.println("Invalid token for user " + mail);
            }
        } else {
            System.out.println("User name is null or authentication is already set");
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
