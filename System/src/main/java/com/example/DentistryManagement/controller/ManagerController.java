package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.ClinicDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/manager")
@RestController
@RequiredArgsConstructor
@Tag(name = "Manager API")
public class ManagerController {
    private final UserService userService;
    private final StaffService staffService;
    private final ClinicService clinicService;
    private final DentistService dentistService;
    private final AuthenticationService authenticationService;
    private final Logger logger = LogManager.getLogger(UserController.class);


    //---------------------------REGISTER STAFF && DENTIST---------------------------

    @Operation(summary = "Register a new staff member")
    @PostMapping("/register/staff")
    public ResponseEntity<AuthenticationResponse> registerStaff(@RequestBody RegisterRequest request,
                                                                @RequestParam String clinicId) {
        try {
            Clinic clinic = clinicService.findClinicByID(clinicId);
            AuthenticationResponse response = authenticationService.registerStaff(request, clinic);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }


    @Operation(summary = "Register a new dentist")
    @PostMapping("/register/dentist")
    public ResponseEntity<AuthenticationResponse> registerDentist(@RequestBody RegisterRequest request,
                                                                  @RequestParam String clinicId,
                                                                  @RequestParam String staffId) {
        try {
            Clinic clinic = clinicService.findClinicByID(clinicId);
            Staff staff = staffService.findStaffById(staffId);
            AuthenticationResponse response = authenticationService.registerDentist(request, clinic, staff);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }


//---------------------------MODIFY CLINIC AND USER---------------------------


    @Operation(summary = "Edit users")
    @PutMapping("/edit/{userID}")
    public ResponseEntity<Client> editUser(@PathVariable String userID, @RequestBody UserDTO userDTO) {
        Client updateUser = userService.findUserById(userID);

        if (updateUser != null) {
            updateUser.setName(userDTO.getName());
            updateUser.setPhone(userDTO.getPhone());
            updateUser.setMail(userDTO.getMail());
            updateUser.setBirthday(userDTO.getBirthday());

            userService.updateUser(updateUser);
            return ResponseEntity.ok(updateUser);
        } else {
            System.out.println("User not fail with userID: " + userID);
            ;
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit clinic")
    @PutMapping("/edit/{clinicID}")
    public ResponseEntity<Clinic> editClinic(@PathVariable String clinicID, @RequestBody ClinicDTO clinicDTO) {
        Clinic updateClinic = clinicService.findClinicByID(clinicID);

        if (updateClinic != null) {
            updateClinic.setPhone(clinicDTO.getPhone());
            updateClinic.setAddress(clinicDTO.getAddress());
            updateClinic.setSlotDuration(clinicDTO.getSlotDuration());
            updateClinic.setOpenTime(clinicDTO.getOpenTime());
            updateClinic.setCloseTime(clinicDTO.getCloseTime());
            updateClinic.setBreakStartTime(clinicDTO.getBreakEndTime());

            clinicService.save(updateClinic);
            return ResponseEntity.ok(updateClinic);
        } else {
            System.out.println("Cannot find: " + clinicID);
            ;
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Set dentist for staff")
    @PutMapping("/set-staff/{staffID}/{dentistID}")
    public ResponseEntity<Dentist> updateStaffForDentist(@PathVariable String dentistID, @PathVariable String staffID) {
        Staff staff;
        Dentist dentist;
        try {
            staff = staffService.findStaffById(staffID);
            dentist = dentistService.findDentistByID(dentistID);

            return ResponseEntity.ok(dentistService.updateStaffForDentist(staff, dentist));
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }


//---------------------------GET ALL STAFF && DENTIST && CLINIC---------------------------

    @Operation(summary = "All Dentists")
    @GetMapping("/all-dentist")
    public ResponseEntity<List<Client>> getAllDentists() {
        try {
            String mail = userService.mailExtract();
            return ResponseEntity.ok(userService.findAllDentistByManager(mail));
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }


    @Operation(summary = "All Staffs")
    @GetMapping("/all-staff")
    public ResponseEntity<List<Client>> getAllStaffs() {
        try {
            String mail = userService.mailExtract();
            return ResponseEntity.ok(userService.findAllStaffByManager(mail));
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "All Clinics")
    @GetMapping("/all-clinic")
    public ResponseEntity<List<Clinic>> getAllClinics() {
        try {
            String mail = userService.mailExtract();

            return ResponseEntity.ok(clinicService.findAllClinicsByManager(mail));
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "List staff dentist manage")
    @GetMapping("/{staffID}/all-dentists")
    public ResponseEntity<List<Dentist>> getDentistByStaff(@PathVariable String staffID) {
        List<Dentist> dentists;
        Staff staff;
        try {
            staff = staffService.findStaffById(staffID);
            dentists = dentistService.findDentistByStaff(staff);
            return ResponseEntity.ok(dentists);
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }
}
