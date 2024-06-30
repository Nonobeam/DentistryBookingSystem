package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.UserRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AppointmentService appointmentService;
    private final UserMapping userMapping;
    private final UserRepository userRepository;

    //----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Manager information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findClientByMail(mail);
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok(userDTO.getUserDTOFromUser(user));
    }

    @Operation(summary = "User update their profile")
    @GetMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody AdminDTO userDTO) {
        try {
            userRepository.findByMail(userService.mailExtract()).ifPresent(userDTO::getUserDTOFromUser);
            return ResponseEntity.ok(userDTO);
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found user");
            logger.error("Not found user", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


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
    @PutMapping("/editWorker")
    public ResponseEntity<?> editUser(@RequestBody UserDTO userDTO) {
        if (userService.isPresentUser(userDTO.getId()).isPresent()) {
            Client updatedUser = userMapping.mapUser(userDTO);
            userService.updateUser(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            ErrorResponseDTO error = new ErrorResponseDTO("403", "User could not be update");
            logger.error("User could not be update");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

        }
    }

    @Operation(summary = "Delete user")
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


    @Operation(summary = "Edit clinic")
    @PutMapping("/editClinic")
    public ResponseEntity<Clinic> editClinic(@RequestBody ClinicDTO clinicDTO) {
        Clinic updateClinic = clinicService.findClinicByID(clinicDTO.getId());

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
            System.out.println("Cannot find: " + clinicDTO.getId());
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Set dentist for staff")
    @PutMapping("/set-staff/{clinicID}")
    public ResponseEntity<?> setUpStaffForDentistInClinic(@PathVariable String clinicID) {
        try {
            List<Client> staff = userService.findAllStaffInClinic(clinicID);
            List<ClinicWorkerDTO> staffList = ClinicWorkerDTO.fromClientList(staff);
            List<Client> dentist = userService.findAllDentistInDentist(clinicID);
            List<ClinicWorkerDTO> dentistList = ClinicWorkerDTO.fromClientList(dentist);

            ClinicWorkerResponseDTO responseDTO = new ClinicWorkerResponseDTO(staffList, dentistList);
            return ResponseEntity.ok(responseDTO);
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
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
    public ResponseEntity<?> getAllDentists() {
        try {
            String mail = userService.mailExtract();
            List<Client> dentists = userService.findAllDentistByManager(mail);
            if (dentists != null && !dentists.isEmpty()) {
                List<AdminDTO> clientDTOs = dentists.stream()
                        .map(client -> {
                            AdminDTO clientDTO = new AdminDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setBirthday(client.getBirthday());
                            clientDTO.setId(client.getUserID());
                            clientDTO.setStatus(client.getStatus());
                            clientDTO.setPassword(client.getPassword());
                            clientDTO.setClinicName(client.getDentist().getClinic().getName());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found any dentist");
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }


    @Operation(summary = "All Staffs")
    @GetMapping("/all-staff")
    public ResponseEntity<?> getAllStaffs() {
        try {
            String mail = userService.mailExtract();
            List<Client> staffList = userService.findAllStaffByManager(mail);
            if (staffList != null && !staffList.isEmpty()) {
                List<AdminDTO> clientDTOs = staffList.stream()
                        .map(client -> {
                            AdminDTO clientDTO = new AdminDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setBirthday(client.getBirthday());
                            clientDTO.setId(client.getUserID());
                            clientDTO.setStatus(client.getStatus());
                            clientDTO.setPassword(client.getPassword());
                            clientDTO.setClinicName(client.getStaff().getClinic().getName());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found any staff");
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "All Clinics")
    @GetMapping("/all-clinic")
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        try {
            String mail = userService.mailExtract();
            List<ClinicDTO> clinicDTOS = clinicService.findAllClinicsByManager(mail).stream()
                    .map(clinic -> new ClinicDTO().clinicMapping(clinic))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(clinicDTOS);
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "List staff dentist manage")
    @GetMapping("/{staffID}/all-dentists")
    public ResponseEntity<?> getDentistByStaff(@PathVariable String staffID) {
        List<Dentist> dentists;
        Staff staff;
        try {
            staff = staffService.findStaffById(staffID);
            dentists = dentistService.findDentistByStaff(staff);
            if (dentists != null && !dentists.isEmpty()) {
                List<AdminDTO> clientDTOs = dentists.stream()
                        .map(client -> {
                            AdminDTO clientDTO = new AdminDTO();
                            clientDTO.setName(client.getUser().getName());
                            clientDTO.setPhone(client.getUser().getPhone());
                            clientDTO.setMail(client.getUser().getMail());
                            clientDTO.setBirthday(client.getUser().getBirthday());
                            clientDTO.setId(client.getUser().getUserID());
                            clientDTO.setStatus(client.getUser().getStatus());
                            clientDTO.setPassword(client.getUser().getPassword());
                            clientDTO.setClinicName(client.getStaff().getClinic().getName());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No staff user found");
        } catch (Error error) {
            throw new Error("Error while getting dentists " + error);
        }
    }

    @Operation(summary = "Manager Dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoardData(@RequestParam("year") int year) {
        try {
            Client manager = userService.findClientByMail(userService.mailExtract());
            if (manager == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, Map<Integer, Long>> yearlyAppointments = appointmentService.getClinicAppointmentsForYear(manager, year);
            int totalAppointmentInMonth = appointmentService.totalAppointmentsInMonthByManager(manager);
            int totalAppointmentInYear = appointmentService.totalAppointmentsInYearByManager(manager);

            DashboardBoss dashboardResponse = new DashboardBoss(null, yearlyAppointments, totalAppointmentInMonth, totalAppointmentInYear);
            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found data in dashboard");
            logger.error("Not found data in dashboard");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }
}
