package com.example.DentistryManagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.DentistryManagement.core.user.Permission.*;
import static com.example.DentistryManagement.core.user.Role.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig{

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
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
            "/swagger-ui/index.html#/**",
            "/user/forgotPassword",
            "/user/resetPassword",
            "/user/resetPassword/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .cors(Customizer.withDefaults())
                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Authorize any request
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/user/**").hasAnyRole(ADMIN.name(), MANAGER.name(), CUSTOMER.name(), STAFF.name(), DENTIST.name())
                                .requestMatchers(GET, "/user/**").hasAnyAuthority(READ.name())
                                .requestMatchers(PUT, "/user/**").hasAnyAuthority(UPDATE.name())
                                .requestMatchers(POST, "/user/**").hasAnyAuthority(WRITE.name())
                                .requestMatchers("/api/v1/manager/**").hasRole(MANAGER.name())
                                .requestMatchers(GET, "/api/v1/manager/**").hasAuthority(READ.name())
                                .requestMatchers(PUT, "/api/v1/manager/**").hasAuthority(UPDATE.name())
                                .requestMatchers(POST, "/api/v1/manager/**").hasAuthority(WRITE.name())
                                .requestMatchers("/api/v1/staff/**").hasRole(STAFF.name())
                                .requestMatchers(GET, "/api/v1/staff/**").hasAuthority(READ.name())
                                .requestMatchers("/api/v1/dentist/**").hasRole(DENTIST.name())
                                .requestMatchers(GET, "/api/v1/dentist/**").hasAuthority(READ.name())
                                .requestMatchers(POST, "/api/v1/dentist/**").hasAuthority(UPDATE.name())
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
        return httpSecurity.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

