package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Admin API")
public class AdminController {
    private final UserService userService;
    private final UserMapping userMapping;


//    @Operation(summary = "Admin")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/dentistList")
//    public ResponseEntity<Optional<List<Client>>> dentistList() {
//        try {
//
//            Optional<List<Client>> clients = userService.findAllDentists();
//            if (clients.isPresent() && clients.get().isEmpty()) {
//                return ResponseEntity.noContent().build();
//            }
//            return ResponseEntity.ok(clients);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Optional.empty());
//        }
//    }

//    @Operation(summary = "Admin")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/customerList")
//    public ResponseEntity<List<Client>> customerList() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authenticationService.isUserAuthorized(authentication, "userId", Role.ADMIN)) {
//                String userId = authentication.getName();
//                List<Client> clients = userService.findAllCustomer();
//                if (clients.isPresent() && clients.get().isEmpty()) {
//                    return ResponseEntity.noContent().build();
//                }
//                return ResponseEntity.ok(clients);
//            } else {
//                // lỗi 403
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Optional.empty());
//        }
//    }

//    @Operation(summary = "Admin")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/staffList")
//    public ResponseEntity<Optional<List<Client>>> staffList() {
//        try {
//
//                Optional<List<Client>> clients = userService.findAllStaffs();
//                if (clients.isPresent() && clients.get().isEmpty()) {
//                    return ResponseEntity.noContent().build();
//                }
//                return ResponseEntity.ok(clients);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Optional.empty());
//        }
//    }

//    @Operation(summary = "Admin")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/managerList")
//    public ResponseEntity<Optional<List<Client>>> managerList() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//
//                List<Client> clients = userService.findAllManagers();
//                if (clients.isPresent() && clients.get().isEmpty()) {
//                    return ResponseEntity.noContent().build();
//                }
//                return ResponseEntity.ok(clients);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Optional.empty());
//        }
//    }

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
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody UserDTO updateduser) {
        try {
            if (userService.isPresentUser(id) !=null) {
                Client client = userMapping.mapUser(updateduser);
                userService.updateUser(client);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be update.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the user.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {

            if (userService.isPresentUser(id) != null) {
                Optional<Client> c = userService.isPresentUser(id);
                if (c.isPresent()) {
                    Client client = c.get();
                    client.setStatus(0);
                    userService.updateUserStatus(client);
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();                    }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be delete.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the user.");
        }
    }
}
