package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class AdminController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/denlist")
    public ResponseEntity<Optional<List<Client>>> denList() {
        try {

            Optional<List<Client>> clients = userService.findAllDen();
            if (clients.isPresent() && clients.get().isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(clients);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/cuslist")
    public ResponseEntity<Optional<List<Client>>> cusList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.ADMIN)) {
                String userId = authentication.getName();
                Optional<List<Client>> clients = userService.findAllCus();
                if (clients.isPresent() && clients.get().isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(clients);
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/stafflist")
    public ResponseEntity<Optional<List<Client>>> staffList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.ADMIN)) {
                String userId = authentication.getName();
                Optional<List<Client>> clients = userService.findAllStaff();
                if (clients.isPresent() && clients.get().isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(clients);
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/managerlist")
    public ResponseEntity<Optional<List<Client>>> managerList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.ADMIN)) {
                String userId = authentication.getName();
                Optional<List<Client>> clients = userService.findAllManager();
                if (clients.isPresent() && clients.get().isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(clients);
            } else {
                // lỗi 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Optional.empty());
        }
    }

//    @Operation(summary = "Admin")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
////    @PostMapping("/newplayer")
//    public ResponseEntity<?> newUser(Client newClient) {
//        try {
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while creating the user.");
//        }
//    }
    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/updateplayer")
    public ResponseEntity<?> updateUser(Client newClient) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.ADMIN)) {
                String userId = authentication.getName();

                if (userService.isPresentUser(newClient.getUserID()) !=null) {

                    Client createdClient = userService.updateUser(newClient);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be update.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the user.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationService.isUserAuthorized(authentication, "userId", Role.ADMIN)) {

                if (userService.isPresentUser(id) != null) {
                    Optional<Client> c = userService.isPresentUser(id);
                    if (c.isPresent()) {
                        Client client = c.get();
                        client.setStatus(0);
                        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserStatus(client));
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();                    }

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be delete.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the user.");
        }
    }


}
