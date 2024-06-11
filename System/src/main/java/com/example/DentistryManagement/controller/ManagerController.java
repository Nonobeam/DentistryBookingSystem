package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.ClinicDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.ClinicService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/manager")
@RestController
@RequiredArgsConstructor
@Tag(name = "Manager API")
public class ManagerController {
    private final UserService userService;
    private final ClinicService clinicService;

    @Operation(summary = "Edit users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/edit/{userID}")
    public ResponseEntity<Client> editUser(@PathVariable String userID, @RequestBody UserDTO userDTO) {
        Client updateUser = userService.findUserByID(userID);

        if(updateUser != null) {

            updateUser.setFirstName(userDTO.getFirstName());
            updateUser.setLastName(userDTO.getLastName());
            updateUser.setPhone(userDTO.getPhone());
            updateUser.setMail(userDTO.getMail());
            updateUser.setBirthday(userDTO.getBirthday());

            userService.save(updateUser);
            return ResponseEntity.ok(updateUser);
        } else {
            System.out.println("User not fail with userID: " + userID);;
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/edit/{clinicID}")
    public ResponseEntity<Clinic> editClinic(@PathVariable String clinicID, @RequestBody ClinicDTO clinicDTO) {
        Clinic updateClinic = clinicService.findClinicByID(clinicID);

        if(updateClinic != null) {
            updateClinic.setPhone(clinicDTO.getPhone());
            updateClinic.setAddress(clinicDTO.getAddress());
            updateClinic.setSlotDuration(clinicDTO.getSlotDuration());
            updateClinic.setOpenTime(clinicDTO.getOpenTime());
            updateClinic.setCloseTime(clinicDTO.getCloseTime());
            updateClinic.setBreakStartTime(clinicDTO.getBreakEndTime());

            clinicService.save(updateClinic);
            return ResponseEntity.ok(updateClinic);
        } else {
            System.out.println("Cannot find: " + clinicID);;
            return ResponseEntity.notFound().build();
        }
    }

//    @Operation(summary = "Clinic user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/denlist")
//    public ResponseEntity<Optional<List<Client>>> denList() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authenticationService.isUserAuthorized(authentication, "userId", Role.MANAGER)) {
//                String userId = authentication.getName();
//                Optional<List<Client>> clients = userService.findAllDenByClinic(userId);
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
//
//    @Operation(summary = "Clinic user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/stafflist")
//    public ResponseEntity<Optional<List<Client>>> staffList() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authenticationService.isUserAuthorized(authentication, "userId", Role.MANAGER)) {
//                String userId = authentication.getName();
//                Optional<List<Client>> clients = userService.findAllStaffByClinic(userId);
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
//
//    @Operation(summary = "Clinic ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    @PostMapping("/cliniclist")
//    public ResponseEntity<Optional<List<Clinic>>> clinicList() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authenticationService.isUserAuthorized(authentication, "userId", Role.MANAGER)) {
//                String userId = authentication.getName();
//                Optional<List<Clinic>> clinics = clinicService.findClinicByManager(userId);
//                if (clinics.isPresent() && clinics.get().isEmpty()) {
//                    return ResponseEntity.noContent().build();
//                }
//                return ResponseEntity.ok(clinics);
//            } else {
//                // lỗi 403
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Optional.empty());
//        }
//    }

}
