package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.AdminDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/admin")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Admin API")
public class AdminController {
    private final UserService userService;
    private final UserMapping userMapping;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AdminDTO convertToAdminDTO(Client client) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(client.getUserID());
        adminDTO.setName(client.getName());
        adminDTO.setPhone(client.getPhone());
        adminDTO.setMail(client.getMail());
        adminDTO.setBirthday(client.getBirthday());
        adminDTO.setPassword(client.getPassword());
        adminDTO.setStatus(client.getStatus());
        if (client.getRole() == Role.DENTIST) {
            Dentist dentist = userService.findDentistByMail(client.getMail());
            adminDTO.setClinicName(dentist.getClinic().getName());
        } else if (client.getRole() == Role.STAFF) {
            Staff staff = userService.findStaffByMail(client.getMail());
            if (staff.getClinic() != null)
                adminDTO.setClinicName(staff.getClinic().getName());
        }
        return adminDTO;
    }

    @Operation(summary = "Admin")
    @GetMapping("/dentistList")
    public ResponseEntity<?> dentistList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findDentistFollowSearching(search);

            } else {
                userList = userService.findAllDentist();
            }
            List<AdminDTO> adminDTOList = userList.stream()
                    .map(this::convertToAdminDTO)
                    .collect(Collectors.toList());
            if (!adminDTOList.isEmpty()) {
                return ResponseEntity.ok(adminDTOList);
            } else return ResponseEntity.ok(" ");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
            logger.error("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/managerList")
    public ResponseEntity<?> managerList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findManagerFollowSearching(search);
            } else {
                userList = userService.findAllManager();
            }
            List<AdminDTO> adminDTOList = userList.stream()
                    .map(this::convertToAdminDTO)
                    .collect(Collectors.toList());
            if (!adminDTOList.isEmpty()) {
                return ResponseEntity.ok(adminDTOList);
            } else
                return ResponseEntity.ok(" ");

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
            logger.error("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @Operation(summary = "Admin")

    @GetMapping("/staffList")
    public ResponseEntity<?> staffList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findStaffFollowSearching(search);
            } else {
                userList = userService.findAllStaff();
            }
            List<AdminDTO> adminDTOList = userList.stream()
                    .map(this::convertToAdminDTO)
                    .collect(Collectors.toList());
            if (!adminDTOList.isEmpty()) {
                return ResponseEntity.ok(adminDTOList);
            } else return ResponseEntity.ok(" ");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
            logger.error("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @Operation(summary = "Admin")
    @GetMapping("/customerList")
    public ResponseEntity<?> customerList(@RequestParam(required = false) String search) {
        try {
            List<Client> userList;
            if (search != null && !search.isEmpty()) {
                userList = userService.findCustomerFollowSearching(search);
            } else {
                userList = userService.findAllCustomer();
            }
            List<AdminDTO> adminDTOList = userList.stream()
                    .map(this::convertToAdminDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(adminDTOList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found");
            logger.error("Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Admin")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody AdminDTO updatedUser) {
        try {
            if (userService.isPresentUser(id).isPresent()) {
                Client client = userMapping.mapUserForAdmin(updatedUser);
                userService.updateUser(client);
                return ResponseEntity.ok(client);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("403", "User could not be update");
                logger.error("User could not be update");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

            }

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {

            if (userService.isPresentUser(id).isPresent()) {
                Optional<Client> c = userService.isPresentUser(id);
                if (c.isPresent()) {
                    Client client = c.get();
                    userService.updateUserStatus(client, 0);
                    return ResponseEntity.ok().build();
                } else {
                    ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
                    logger.error("User not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                }

            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("403", "User could not be deleted");
                logger.error("User could not be deleted");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
