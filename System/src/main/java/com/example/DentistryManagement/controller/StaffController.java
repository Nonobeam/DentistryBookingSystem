package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.DashboardResponse;
import com.example.DentistryManagement.DTO.UserAppointDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;

import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.user.Client;

//import com.example.DentistryManagement.core.mail.Mail;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Staff API")
public class StaffController {
    // private MailService emailService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final DentistScheduleService dentistScheduleService;
    private final ServiceService serviceService;
    private final DentistService dentistService;
    private final StaffService staffService;
//---------------------------GET ALL SERVICES, CLINIC IN THE WORKING CLINIC---------------------------


    @Operation(summary = "All Services in System")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/all-services")
    public ResponseEntity<List<Services>> getAllServices() {
        try {
            Staff staff =userService.findStaffByMail( userService.mailExtract());
            Clinic clinic = staff.getClinic();

            return ResponseEntity.ok(serviceService.findServicesByClinic(clinic.getClinicID()));
        } catch (Error error) {
            return ResponseEntity.badRequest().build();
        }
    }
    @Operation(summary = "All Services in System")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/set-service/{dentistID}")
    public ResponseEntity<Dentist> updateDentistService(@PathVariable String dentistID, @RequestParam String serviceID) {
        Dentist dentist;
        Services service;
        try {
            dentist = dentistService.findDentistByID(dentistID);
            service = serviceService.findServiceByID(serviceID);
            dentist.getServicesList().add(service);

            return ResponseEntity.ok(dentist);
        } catch (Error error) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "All Dentists manage by a Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/dentist/all")
    public ResponseEntity<List<Dentist>> getAllDentists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();

        Staff staff;
        List<Dentist> dentists;
        try {
            staff = staffService.findStaffByMail(mail);
            dentists = dentistService.findDentistByStaff(staff);
            return ResponseEntity.ok(dentists);
        } catch (Error error) {
            return ResponseEntity.badRequest().build();
        }
    }


//---------------------------MANAGE DENTIST---------------------------
@Operation(summary = "Staff")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully"),
        @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Error")

})
@GetMapping("/dentistList")
public ResponseEntity<Optional<List<UserDTO>>> findDentistManage() {
    try {

        String mail = userService.mailExtract();

        Optional<List<Client>> clientsOptional = userService.findDentistByStaff(mail);

        // Kiểm tra nếu danh sách clients không rỗng và tồn tại
        if (clientsOptional.isPresent() && !clientsOptional.get().isEmpty()) {
            // Chuyển đổi danh sách Client sang danh sách ClientDTO
            List<UserDTO> clientDTOs = clientsOptional.get().stream()
                    .map(client -> {
                        UserDTO clientDTO = new UserDTO();
                        clientDTO.setFirstName(client.getFirstName());
                        clientDTO.setPhone(client.getPhone());
                        clientDTO.setMail(client.getMail());
                        clientDTO.setLastName(client.getLastName());
                        clientDTO.setBirthday(client.getBirthday());

                        return clientDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Optional.of(clientDTOs));

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    } catch (Exception e) {
        // Xử lý ngoại lệ
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}  @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/dentist/{id}")
    public ResponseEntity<?> findDentistInformationByStaff(@PathVariable("id") String id) {
        try {

            UserDTO userDTO =new UserDTO();
            Client client= userService.userInfo(id);
            userDTO.setFirstName(client.getFirstName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setLastName(client.getLastName());
            userDTO.setBirthday(client.getBirthday());

            Optional<List<Appointment>> appointment=appointmentService.dentistAppointment(id);
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointment);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Set Dentist Schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set the schedule"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/set-schedule")
    public ResponseEntity<?> setDentistSchedule(@RequestParam String dentistID,
                                                @RequestParam LocalDate startDate,
                                                @RequestParam LocalDate endDate,
                                                @RequestParam String timeSlotID,
                                                @RequestParam String clinicID,
                                                @RequestParam String serviceID) {
        try {
            dentistScheduleService.setDentistSchedule(dentistID, startDate, endDate, timeSlotID, clinicID, serviceID);
            return ResponseEntity.ok("Schedule set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @Operation(summary = "Delete Dentist Schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the schedule"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete-schedule")
    public ResponseEntity<?> deleteDentistSchedule(@RequestParam String dentistID,
                                                   @RequestParam LocalDate workDate) {
        try {
            dentistScheduleService.deleteDentistSchedule(dentistID, workDate);
            return ResponseEntity.ok("Schedule deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //---------------------------MANAGE CUSTOMER---------------------------
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/customerList")
    public ResponseEntity<Optional<List<UserDTO>>> findAllCusManage() {
        try {
            String mail = userService.mailExtract();

            Optional<List<Client>> clientsOptional = userService.findCustomerinClinic(mail);

            // Kiểm tra nếu danh sách clients không rỗng và tồn tại
            if (clientsOptional.isPresent() && !clientsOptional.get().isEmpty()) {

                List<UserDTO> clientDTOs = clientsOptional.get().stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setFirstName(client.getFirstName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setLastName(client.getLastName());
                            clientDTO.setBirthday(client.getBirthday());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(Optional.of(clientDTOs));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> findAllCusByStaff(@PathVariable("id") String id) {
        try {
            String mail = userService.mailExtract();

            UserDTO userDTO =new UserDTO();
            Client client= userService.userInfo(id);
            userDTO.setFirstName(client.getFirstName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setLastName(client.getLastName());
            userDTO.setBirthday(client.getBirthday());
            Optional<List<Appointment>> appointment=appointmentService.customerAppointment(id,mail);
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointment);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //---------------------------MANAGE APPOINTMENT---------------------------
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })

    @GetMapping()
    public ResponseEntity<?> receiveNotification() {
        try {
            String mail = userService.mailExtract();

            Optional<List<Notification>> notice = notificationService.receiveNotice(mail) ;

            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/appointment-history/{appointmentid}")
    public ResponseEntity<Appointment> setAppointmentStatus(@PathVariable("appointmentid") String appointmentid,@RequestParam("status") int status) {

        try {
            Appointment appointment= appointmentService.findAppointmentById(appointmentid);
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/available-service")
    public ResponseEntity<List<Services>> getAvailableServices(
            @RequestParam LocalDate bookDate,
            @RequestParam Clinic clinic) {

        List<Services> dentistService;
        try {
            dentistService = dentistScheduleService
                    .getServiceNotNullByDate(bookDate, clinic);
            return ResponseEntity.ok(dentistService);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Show available schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{clinicID}/available-schedules")
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedules(
            @RequestParam LocalDate workDate,
            @PathVariable String clinicID,
            @RequestParam String servicesId) {

        Optional<List<DentistSchedule>> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinicID);

        return dentistScheduleList
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }


    @Operation(summary = "Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/make-booking")
    public ResponseEntity<Appointment> makeBooking(@RequestBody DentistSchedule dentistSchedule, @RequestParam String customerid, @RequestParam Optional<String> dependentid ) {
        try {
            String mail = userService.mailExtract();
            Client customer = userService.findUserById(customerid);
            if(!dependentid.isPresent() || !dependentid.isEmpty()){
                Dependent dependent = userService.findDependentById(dependentid);
            }
            Staff staff = userService.findStaffByMail(mail);
            if (appointmentService.findAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5) {
                throw new Error("Over booking in today!");
            }

            if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
                throw new Error("Over appointment in this date!");
            }
            Appointment newAppointment = new Appointment();
            newAppointment.setUser(customer);
            newAppointment.setServices(dentistSchedule.getServices());
            newAppointment.setClinic(dentistSchedule.getClinic());
            newAppointment.setDate(dentistSchedule.getWorkDate());
            newAppointment.setTimeSlot(dentistSchedule.getTimeslot());
            newAppointment.setStaff(staff);
            dentistSchedule.setAvailable(0);
            dentistScheduleService.setAvailableDentistSchedule(dentistSchedule);
            return ResponseEntity.ok(newAppointment);
        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/update-appointment")
    public ResponseEntity <Appointment> updateAppointment(@RequestBody Appointment appointment) {
        try {
            return ResponseEntity.ok(appointmentService.updateAppointment(appointment));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/appointment-history")
    public ResponseEntity<Optional<List<Appointment>>> findAllAppointmentHistory() {
        try {
            String mail = userService.mailExtract();

            return ResponseEntity.ok(appointmentService.findApointmentclinic(mail));
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/appointment-history/{appointmentid}")
    public ResponseEntity<Appointment> setAppointmentStatus(@RequestParam("status") int status, @PathVariable("appointmentid") String appointmentid) {

        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentid);
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/appointment-history")
    public ResponseEntity<Optional<List<Appointment>>> searchAppointmentByStaff(@RequestParam("searchAppointment") LocalDate date, @RequestParam("name") String name) {
        try {
            return ResponseEntity.ok(appointmentService.searchAppointmentByWorker(date,name));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboardData(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("year") int year) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, List<Appointment>> dailyAppointments = appointmentService.getDailyAppointmentsByDentist(date,staff);
            Map<Integer, Long> monthlyAppointments = appointmentService.getAppointmentsByStaffForYear(staff, year);

            DashboardResponse dashboardResponse = new DashboardResponse(dailyAppointments, monthlyAppointments);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @Operation(summary = "Send mail for user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully deleted the schedule"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @PostMapping("/{dentistID}/send-email")
//    public String sendEmail(@PathVariable String dentistID, @RequestBody Mail mail) {
//        emailService.sendSimpleMessage(mail.getTo(), mail.getSubject(), mail.getText());
//        return "Email sent successfully";
//    }
}