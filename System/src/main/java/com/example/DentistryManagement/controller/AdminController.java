package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.ClinicService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Admin API")
public class AdminController {
    private final UserService userService;
    private final UserMapping userMapping;

    private final ClinicService clinicService;

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/dentistList")
    public ResponseEntity<Optional<List<Client>>> dentistList(@RequestParam(required = false) String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findDentistInClinic(search);

            } else {
                userList = userService.findAllDentist();
            }
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/managerList")
    public ResponseEntity<Optional<List<Client>>> managerList(@RequestParam(required = false) String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.searchManager(search);
            } else {
                userList = userService.findAllManager();
            }
            return ResponseEntity.ok(userList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/staffList")
    public ResponseEntity<Optional<List<Client>>> staffList(@RequestParam(required = false) String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findStaffInClinic(search);
            } else {
                userList = userService.findAllStaff();
            }
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/customerList")
    public ResponseEntity<Optional<List<Client>>> customerList(@RequestParam(required = false) String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.searchCustomer(search);
            } else {
                userList = userService.findAllCustomer();
            }
            return ResponseEntity.ok(userList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


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
            if (userService.isPresentUser(id) != null) {
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
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be delete.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the user.");
        }
    }

    @GetMapping("/dentistList/")
    public ResponseEntity<Optional<List<Client>>> sortDentist(@RequestParam("search") String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null) {
                userList = userService.findDentistInClinic(search);
                return ResponseEntity.ok(userList);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/managerList/")
    public ResponseEntity<Optional<List<Client>>> sortManager(@RequestParam("search") String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null) {
                userList = userService.searchManager(search);
                return ResponseEntity.ok(userList);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/staffList/")
    public ResponseEntity<Optional<List<Client>>> sortStaff(@RequestParam("search") String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null) {
                userList = userService.findStaffInClinic(search);
                return ResponseEntity.ok(userList);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/customerList/")
    public ResponseEntity<Optional<List<Client>>> sortCustomer(@RequestParam("search") String search) {
        try {
            Optional<List<Client>> userList;
            if (search != null) {
                userList = userService.searchCustomer(search);
                return ResponseEntity.ok(userList);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
