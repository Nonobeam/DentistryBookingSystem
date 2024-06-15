package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.ClinicDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.service.AuthenticationService;
import com.example.DentistryManagement.service.ClinicService;
import com.example.DentistryManagement.service.DentistService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/manager")
@RestController
@RequiredArgsConstructor
@Tag(name = "Manager API")
public class ManagerController {
    private final UserService userService;
    private final DentistService dentistService;
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

    @Operation(summary = "All Dentists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/all-dentist")
    public ResponseEntity<List<Client>> getAllDentists() {
        try {
            return ResponseEntity.ok(userService.findAllDentists());
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "All Staffs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/all-staff")
    public ResponseEntity<List<Client>> getAllStaffs() {
        try {
            return ResponseEntity.ok(userService.findAllStaffs());
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "All Clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/all-clinic")
    public ResponseEntity<List<Clinic>> getAllClinics() {
        try {
            return ResponseEntity.ok(clinicService.findAllClinics());
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

}
