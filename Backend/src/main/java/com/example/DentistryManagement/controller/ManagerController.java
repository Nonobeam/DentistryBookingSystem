package com.example.DentistryManagement.controller;
import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.user.Role;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.auth.AuthenticationResponse;
import com.example.DentistryManagement.auth.RegisterRequest;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.service.*;
import com.example.DentistryManagement.service.AppointmentService.AppointmentAnalyticService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentService;
import com.example.DentistryManagement.service.UserService.UserDentistService;
import com.example.DentistryManagement.service.UserService.UserService;
import com.example.DentistryManagement.service.UserService.UserStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/manager")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Manager API")
public class ManagerController {
    private final UserService userService;
    private final UserStaffService staffService;
    private final UserDentistService dentistService;
    private final ClinicService clinicService;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AppointmentService appointmentService;
    private final UserMapping userMapping;
    private final TimeSlotService timeSlotService;
    private final DentistScheduleService dentistScheduleService;
    private final AppointmentAnalyticService appointmentAnalyticService;


    //----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Manager information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findUserByMail(mail);
        return ResponseEntity.ok(userMapping.getUserDTOFromUser(user));
    }

    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            Client currentUser = userService.findUserByMail(userService.mailExtract());
            if (currentUser == null) {
                return ResponseEntity.status(403).body(new ErrorResponseDTO("403", "Cannot find user"));
            }
            userService.updateUser(userDTO, currentUser);
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
    public ResponseEntity<?> registerStaff(@RequestBody RegisterRequest request,
                                           @RequestParam String clinicId) {
        try {
            Clinic clinic = clinicService.findClinicByID(clinicId);
            AuthenticationResponse response = authenticationService.registerStaff(request, clinic);
            return ResponseEntity.ok(response);
        } catch (Error e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    @Operation(summary = "Register a new dentist")
    @PostMapping("/register/dentist")
    public ResponseEntity<?> registerDentist(@RequestBody RegisterRequest request,
                                             @RequestParam String clinicId,
                                             @RequestParam String staffId) {
        try {
            Clinic clinic = clinicService.findClinicByID(clinicId);
            Staff staff = staffService.findStaffById(staffId);
            AuthenticationResponse response = authenticationService.registerDentist(request, clinic, staff);
            return ResponseEntity.ok(response);
        } catch (Error e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    //---------------------------MODIFY CLINIC AND USER---------------------------


    @Operation(summary = "Edit users")
    @PutMapping("/editWorker")
    public ResponseEntity<?> editUser(@RequestBody UserDTO userDTO) {
        if (userService.isPresentUser(userDTO.getId()).isPresent()) {
            Client updatedUser = userService.findUserById(userDTO.getId());
            userService.updateUser(userDTO, updatedUser);
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

            Optional<Client> optionalClient = userService.isPresentUser(id);
            if (optionalClient.isPresent()) {
                Client client = optionalClient.get();
                if(client.getRole() == Role.STAFF){
                    Staff staff = staffService.findStaffById(client.getUserID());
                    List<Dentist> dentistList = dentistService.findDentistListByStaff(staff);
                    if(!dentistList.isEmpty()){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Staff still manages dentists");
                    }
                }
                if(client.getRole() == Role.DENTIST){
                    Dentist dentist = dentistService.findDentistByID(client.getUserID());
                    List<Appointment> appointmentList = appointmentAnalyticService.getAppointmentByDentistAndStatus(dentist, 1);
                    if(!appointmentList.isEmpty()){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Dentist still has available schedule");
                    }
                }
                userService.updateUserStatus(client, 0);
                return ResponseEntity.ok("Delete user successfully");
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "User not found");
                logger.error("User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }


        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PostMapping("/create-clinic")
    public ResponseEntity<?> createClinic(@RequestBody ClinicDTO clinicDTO) {

        if (!clinicService.checkSlotDurationValid(clinicDTO.getSlotDuration())) {
            return ResponseEntity.status(400).body("Slot duration must be between 30 to 180 minutes");
        }

        Client manager = userService.findUserByMail(userService.mailExtract());

        Clinic clinic = Clinic.builder()
                .name(clinicDTO.getName())
                .phone(clinicDTO.getPhone())
                .address(clinicDTO.getAddress())
                .slotDuration(clinicDTO.getSlotDuration())
                .openTime(clinicDTO.getOpenTime())
                .closeTime(clinicDTO.getCloseTime())
                .breakStartTime(clinicDTO.getBreakStartTime())
                .breakEndTime(clinicDTO.getBreakEndTime())
                .status(clinicDTO.getStatus())
                .user(manager)
                .build();

        Clinic createdClinic = clinicService.save(clinic);

        timeSlotService.createAndSaveTimeSlots(LocalDate.now(), createdClinic,
                createdClinic.getOpenTime(), createdClinic.getCloseTime(),
                createdClinic.getBreakStartTime(), createdClinic.getBreakEndTime(), createdClinic.getSlotDuration());

        return ResponseEntity.ok(createdClinic);
    }


    // Choose the last appointment date
    // Create new timeslot
    @Operation(summary = "Edit clinic")
    @PutMapping("/editClinic")
    public ResponseEntity<?> editClinic(@RequestBody ClinicDTO clinicDTO) {
        Clinic updateClinic = clinicService.findClinicByID(clinicDTO.getId());
        if (!clinicService.checkSlotDurationValid(clinicDTO.getSlotDuration())) {
            return ResponseEntity.status(400).body("Slot duration must be between 30 to 180 minutes");
        }

        if (updateClinic != null) {
            updateClinic.setPhone(clinicDTO.getPhone());
            updateClinic.setAddress(clinicDTO.getAddress());
            updateClinic.setName(clinicDTO.getName());
            updateClinic.setStatus(clinicDTO.getStatus());

            //Check changes in clinic schedule
            if (!updateClinic.getSlotDuration().equals(clinicDTO.getSlotDuration()) ||
                    !updateClinic.getOpenTime().equals(clinicDTO.getOpenTime()) ||
                    !updateClinic.getCloseTime().equals(clinicDTO.getCloseTime()) ||
                    !updateClinic.getBreakStartTime().equals(clinicDTO.getBreakStartTime()) ||
                    !updateClinic.getBreakEndTime().equals(clinicDTO.getBreakEndTime())) {

                updateClinic.setSlotDuration(clinicDTO.getSlotDuration());
                updateClinic.setOpenTime(clinicDTO.getOpenTime());
                updateClinic.setCloseTime(clinicDTO.getCloseTime());
                updateClinic.setBreakStartTime(clinicDTO.getBreakStartTime());
                updateClinic.setBreakEndTime(clinicDTO.getBreakEndTime());

                // Find the date that will apply new time slot
                LocalDate lastDate = appointmentService.startUpdateTimeSlotDate(updateClinic.getClinicID());

                // Delete all dentist-schedule after the apply date of new time slot
                dentistScheduleService.deleteDentistSchedulesAfterDate(lastDate, clinicDTO.getId());

                timeSlotService.createAndSaveTimeSlots(lastDate.plusDays(1), updateClinic,
                        updateClinic.getOpenTime(), updateClinic.getCloseTime(),
                        updateClinic.getBreakStartTime(), updateClinic.getBreakEndTime(), updateClinic.getSlotDuration());

            }

            clinicService.save(updateClinic);
            return ResponseEntity.ok(updateClinic);
        } else {
            System.out.println("Cannot find clinic with ID: " + clinicDTO.getId());
            return ResponseEntity.status(400).body("Cannot find clinic with ID: " + clinicDTO.getId());
        }
    }


    @Operation(summary = "Set dentist for staff")
    @GetMapping("/set-staff/{clinicID}")
    public ResponseEntity<?> setUpStaffForDentistInClinic(@PathVariable String clinicID) {
        try {
            List<Client> staff = staffService.findAllStaffInClinic(clinicID);
            List<ClinicWorkerDTO> staffList = ClinicWorkerDTO.fromClientList(staff);
            List<Client> dentist = dentistService.findAllDentistInClinic(clinicID);
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
            List<Client> dentistList = dentistService.findAllDentistByManager(mail);
            if (dentistList != null && !dentistList.isEmpty()) {
                List<DentistResponseDTO> dentistDTOList = dentistList.stream()
                        .map(userMapping::convertToDentistDTO)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(dentistDTOList);
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
            List<Client> staffList = staffService.findAllStaffByManager(mail);
            if (staffList != null && !staffList.isEmpty()) {
                List<StaffResponseDTO> staffDTOList = staffList.stream()
                        .map(userMapping::convertToStaffDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(staffDTOList);
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
        List<Dentist> dentistList;
        Staff staff;
        try {
            staff = staffService.findStaffById(staffID);
            dentistList = dentistService.findDentistListByStaff(staff);
            if (dentistList != null && !dentistList.isEmpty()) {
                List<DentistResponseDTO> clientDTOs = dentistList.stream()
                        .map(client -> {
                            DentistResponseDTO clientDTO = new DentistResponseDTO();
                            clientDTO.setName(client.getUser().getName());
                            clientDTO.setPhone(client.getUser().getPhone());
                            clientDTO.setMail(client.getUser().getMail());
                            clientDTO.setBirthday(client.getUser().getBirthday());
                            clientDTO.setId(client.getUser().getUserID());
                            clientDTO.setStatus(client.getUser().getStatus());
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
    public ResponseEntity<?> getDashBoardData(@RequestParam(required = false) Integer year) {
        try {
            Client manager = userService.findUserByMail(userService.mailExtract());
            if (manager == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (year == null) year = LocalDate.now().getYear();
            Map<String, Map<Integer, Long>> yearlyAppointments = appointmentAnalyticService.getAppointmentsByYearAndManager(manager, year);
            int totalAppointmentInMonth = appointmentAnalyticService.totalAppointmentsInMonthByManager(manager);
            int totalAppointmentInYear = appointmentAnalyticService.totalAppointmentsInYearByManager(manager);
             Map<String, Double>ratingDentist= appointmentAnalyticService.getRatingDentistByManager(manager);

            DashboardBoss dashboardResponse = new DashboardBoss(null, yearlyAppointments, totalAppointmentInMonth, totalAppointmentInYear,ratingDentist);
            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found data in dashboard");
            logger.error("Not found data in dashboard");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }
}
