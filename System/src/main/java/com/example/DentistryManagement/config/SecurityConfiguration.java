package com.example.DentistryManagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@CrossOrigin(origins = "*", maxAge = 3600)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html#/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .cors(withDefaults())
                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Authorize any request
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL)
                        .permitAll()
//                        .requestMatchers(GET, "/api/v1/**").hasAnyAuthority(ADMIN_READ.name())
//                        .requestMatchers(POST, "/api/v1/**").hasAnyAuthority(ADMIN_CREATE.name())
//                        .requestMatchers(PUT, "/api/v1/**").hasAnyAuthority(ADMIN_UPDATE.name())
//                        .requestMatchers(DELETE, "/api/v1/**").hasAnyAuthority(ADMIN_DELETE.name())
                        .anyRequest()
                        .authenticated()
                )
                // Make session as STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS)
                )
                // Specify authentication provider
                .authenticationProvider(authenticationProvider)
                // Add JWT authentication filter before specified authentication filter class
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

