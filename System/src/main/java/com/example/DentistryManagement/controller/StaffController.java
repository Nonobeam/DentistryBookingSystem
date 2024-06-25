package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.AppointmentDTO;
import com.example.DentistryManagement.DTO.DashboardResponse;
import com.example.DentistryManagement.DTO.UserAppointDTO;
import com.example.DentistryManagement.DTO.UserDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Staff API")
public class StaffController {
    private final MailService emailService;
    private final UserService userService;
    private final ClinicService clinicService;
    private final ServiceService serviceService;
    private final DentistService dentistService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final DentistScheduleService dentistScheduleService;
    private final AppointmentRepository appointmentRepository;
    private final Logger LOGGER = LogManager.getLogger(UserController.class);



//---------------------------MANAGE DENTIST---------------------------

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


    @Operation(summary = "Staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping("/dentistList")
    public ResponseEntity<List<UserDTO>> findAllDentistByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clientsOptional = null;
            if (search != null && !search.isEmpty()) {
                clientsOptional = userService.searchDentistByStaff(mail, search);

            } else clientsOptional = userService.findDentistByStaff(mail);

            if (!clientsOptional.isEmpty()) {
                List<UserDTO> clientDTOs = clientsOptional.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setName(client.getName());
                            clientDTO.setBirthday(client.getBirthday());

                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            } else {
                return ResponseEntity.noContent().build();
            }
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
    @GetMapping("/dentistList/{mail}")
    public ResponseEntity<UserAppointDTO> findDentistInformationByStaff(@PathVariable("mail") String dentistMail) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(dentistMail);

            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());

            List<Appointment> appointmentList = appointmentService.findAllAppointmentByDentist(client.getUserID());
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentriu -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentriu.getServices().getName());
                        appointment.setStatus(appointmentriu.getStatus());
                        appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                        if (appointmentriu.getStaff() != null) {
                            if (appointmentriu.getUser() != null) {
                                appointment.setUser(appointmentriu.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            }
                        } else {
                            if (appointmentriu.getDependent() != null) {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            } else
                                appointment.setUser(appointmentriu.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
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
                                                @RequestParam String timeSlotID) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            dentistScheduleService.setDentistSchedule(dentistID, startDate, endDate, timeSlotID, clinic.getClinicID());
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
    public ResponseEntity<List<UserDTO>> findAllCustomerByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients = null;

            if (search != null && !search.isEmpty()) {
                clients = userService.searchCustomerInClinicByStaff(mail, search);
            } else {
                clients = userService.findCustomerInClinicByStaff(mail);
            }

            if (clients != null && !clients.isEmpty()) {
                List<UserDTO> clientDTOs = clients.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setBirthday(client.getBirthday());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            } else {
                return ResponseEntity.noContent().build();
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
    @GetMapping("/customerList/{mail}")
    public ResponseEntity<UserAppointDTO> findCustomerByStaff(@PathVariable("mail") String customerMail) {
        try {
            String mail = userService.mailExtract();

            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(customerMail);
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());
            List<Appointment> appointmentList = appointmentService.customerAppointment(client.getUserID(), mail);
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentriu -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setServices(appointmentriu.getServices().getName());
                        appointment.setStatus(appointmentriu.getStatus());
                        appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                        if (appointmentriu.getStaff() != null) {
                            if (appointmentriu.getUser() != null) {
                                appointment.setUser(appointmentriu.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            }
                        } else {
                            if (appointmentriu.getDependent() != null) {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            } else
                                appointment.setUser(appointmentriu.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);

        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//---------------------------MANAGE APPOINTMENT---------------------------


    @Operation(summary = "Send mail for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the schedule"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{notificationID}/send-email")
    public ResponseEntity<?> sendEmail(@PathVariable String notificationID,
                                       @RequestParam String mail,
                                       @RequestParam String subject,
                                       @RequestParam String text) {
        Optional<Notification> optionalNotification = notificationService.findNotificationByIDAndStatus(notificationID, 0);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();

            emailService.sendSimpleMessage(mail, subject, text);
            notification.setStatus(1);

            notificationService.save(notification);
            return ResponseEntity.ok("Mail send successfully");
        } else {
            throw new IllegalArgumentException("Notification not found with ID: " + notificationID);
        }
    }


    @Operation(summary = "Get all notification in the clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error")

    })
    @GetMapping()
    public ResponseEntity<List<Notification>> receiveNotification() {
        try {
            String mail = userService.mailExtract();

            List<Notification> notice = notificationService.receiveNotice(mail);

            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/booking/all-service")
    public ResponseEntity<HashMap<String, Services>> getAllServiceByClinic(@RequestParam LocalDate workDate) {
        try {
            HashMap<String, Services> servicesByClinic = new HashMap<>();
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            List<DentistSchedule> dentistScheduleList = clinic.getDentistScheduleList();
            for (DentistSchedule dentistSchedule : dentistScheduleList) {
                if (dentistSchedule.getWorkDate().equals(workDate)) {
                    if (dentistSchedule.getAvailable() == 1) {
                        servicesByClinic.put(dentistSchedule.getServices().getServiceID(), dentistSchedule.getServices());
                    }
                }
            }
            return ResponseEntity.ok(servicesByClinic);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @Operation(summary = "Show available schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/booking/available-schedules")
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedules(
            @RequestParam LocalDate workDate,
            @RequestParam String servicesId) {
        Staff staff = userService.findStaffByMail(userService.mailExtract());
        Clinic clinic = staff.getClinic();
        Optional<List<DentistSchedule>> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinic.getClinicID());

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
    @PostMapping("/booking/make-booking/{dentistScheduleId}")
    public ResponseEntity<Appointment> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail) {
        try {
            Client client = userService.findClientByMail(userService.mailExtract());
            if(userService.findClientByMail(customerMail) != null){
                DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
                if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
                    throw new Error("Over booked for today!");
                }

                if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
                    throw new Error("Full appointment for this date!");
                }
                Appointment newAppointment = new Appointment();

                newAppointment.setStaff(client.getStaff());
                newAppointment.setUser(userService.findClientByMail(customerMail));
                newAppointment.setServices(dentistSchedule.getServices());
                newAppointment.setClinic(dentistSchedule.getClinic());
                newAppointment.setDate(dentistSchedule.getWorkDate());
                newAppointment.setTimeSlot(dentistSchedule.getTimeslot());
                newAppointment.setDentist(dentistSchedule.getDentist());
                newAppointment.setDentistScheduleId(dentistScheduleId);
                newAppointment.setStatus(1);
                if (dependentID != null) {
                    Dependent dependent = userService.findDependentByDependentId(dependentID);
                    newAppointment.setDependent(dependent);
                }
                dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
                Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
                otherSchedule.ifPresent(schedules -> {
                    schedules.forEach(schedule -> schedule.setAvailable(0));
                });
                appointmentRepository.save(newAppointment);
                return ResponseEntity.ok(newAppointment);

            }else return ResponseEntity.noContent().build();

        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Delete appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/delete-booking/{appointmentId}")
    public ResponseEntity<String> deleteBooking(@PathVariable String appointmentId) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            String dentistScheduleId = appointment.getDentistScheduleId();
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            //Check for duplicate cancelled just in case
            if (appointment.getStatus() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment has already been cancelled");
            }
            appointment.setStatus(0);
            Optional<List<DentistSchedule>> unavailableSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 0);
            unavailableSchedule.ifPresent(schedules -> {
                schedules.forEach(schedule -> schedule.setAvailable(1));
            });
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment has been cancelled");
        } catch (Error e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    @GetMapping("/update-booking/{appointmentId}/all-service")
    public ResponseEntity<HashMap<String, Services>> getAllServiceToUpdateByClinic(@RequestParam LocalDate workDate, @PathVariable("appointmentId") String appointmentId) {
        try {
            HashMap<String, Services> servicesByClinic = new HashMap<>();
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            List<DentistSchedule> dentistScheduleList = clinic.getDentistScheduleList();
            for (DentistSchedule dentistSchedule : dentistScheduleList) {
                if (dentistSchedule.getWorkDate().equals(workDate)) {
                    if (dentistSchedule.getAvailable() == 1) {
                        servicesByClinic.put(dentistSchedule.getServices().getServiceID(), dentistSchedule.getServices());
                    }
                }
            }
            servicesByClinic.put((appointment.getServices().getServiceID()), appointment.getServices());
            return ResponseEntity.ok(servicesByClinic);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }


    @Operation(summary = "Show available schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/update-booking/{appointmentId}/available-schedules")
    public ResponseEntity<List<DentistSchedule>> getAvailableSchedulesToUpdate(
            @RequestParam LocalDate workDate,
            @PathVariable String appointmentId,
            @RequestParam String servicesId) {
        List<DentistSchedule> dentistScheduleList;
        Appointment appointment = appointmentService.findAppointmentById(appointmentId);
        Optional<List<DentistSchedule>> optionalDentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, appointment.getClinic().getClinicID());

        DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(appointment.getDentistScheduleId());
        if (optionalDentistScheduleList.isPresent()) {
            dentistScheduleList = optionalDentistScheduleList.get();
            dentistScheduleList.add(dentistSchedule);
        } else {
            dentistScheduleList = new ArrayList<>();
            dentistScheduleList.add(dentistSchedule);
        }
        return ResponseEntity.ok(dentistScheduleList);
    }


    @Operation(summary = "Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/update-booking/{appointmentId}/{dentistScheduleId}")
    public ResponseEntity<Appointment> makeBookingToUpdate(@PathVariable String dentistScheduleId, @PathVariable String appointmentId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail) {
        try {
            Client client = userService.findClientByMail(userService.mailExtract());
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            deleteBooking(appointmentId);
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            if (appointmentService.findAppointmentsByUserAndStatus(client, 1).map(List::size).orElse(5) >= 5) {
                throw new Error("Over booked for today!");
            }

            if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).map(List::size).orElse(10) >= 10) {
                throw new Error("Full appointment for this date!");
            }
            Appointment newAppointment = new Appointment();
            newAppointment.setStaff(client.getStaff());
            newAppointment.setUser(userService.findClientByMail(customerMail));
            newAppointment.setServices(dentistSchedule.getServices());
            newAppointment.setClinic(dentistSchedule.getClinic());
            newAppointment.setDate(dentistSchedule.getWorkDate());
            newAppointment.setTimeSlot(dentistSchedule.getTimeslot());
            newAppointment.setDentist(dentistSchedule.getDentist());
            newAppointment.setDentistScheduleId(dentistScheduleId);
            newAppointment.setStatus(1);
            if (dependentID != null) {
                Dependent dependent = userService.findDependentByDependentId(dependentID);
                newAppointment.setDependent(dependent);
            }
            dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
            Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
            otherSchedule.ifPresent(schedules -> {
                schedules.forEach(schedule -> schedule.setAvailable(0));
            });
            appointmentRepository.save(newAppointment);
            return ResponseEntity.ok(newAppointment);
        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
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
    public ResponseEntity<Appointment> setAppointmentStatus(@PathVariable("appointmentid") String appointmentid, @RequestParam("status") int status) {

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
    @GetMapping("/appointment-history")
    public ResponseEntity<List<AppointmentDTO>> appointmentHistoryByStaff(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList;
            if (date != null || (search != null && !search.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByStaff(date, search, mail);
            } else appointmentList = appointmentService.findApointmentClinic(mail);
            List<AppointmentDTO> appointmentDTOList = appointmentList.stream()
                    .map(appointmentriu -> {
                        AppointmentDTO appointment = new AppointmentDTO();
                        appointment.setAppointmentId(appointmentriu.getAppointmentID());
                        appointment.setServices(appointmentriu.getServices().getName());
                        appointment.setStatus(appointmentriu.getStatus());
                        appointment.setTimeSlot(appointmentriu.getTimeSlot().getStartTime());
                        if (appointmentriu.getStaff() != null) {
                            if (appointmentriu.getUser() != null) {
                                appointment.setUser(appointmentriu.getUser().getName());
                            } else {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            }
                        } else {
                            if (appointmentriu.getDependent() != null) {
                                appointment.setDependent(appointmentriu.getDependent().getName());
                            } else
                                appointment.setUser(appointmentriu.getUser().getName());
                        }

                        return appointment;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashBoardData(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("year") int year) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<String, List<Appointment>> dailyAppointments = appointmentService.getDailyAppointmentsByDentist(date, staff);
            Map<Integer, Long> monthlyAppointments = appointmentService.getAppointmentsByStaffForYear(staff, year);
            int totalAppointmentInMonth = appointmentService.totalAppointmentsInMonthByStaff(staff);
            int totalAppointmentInYear = appointmentService.totalAppointmentsInYearByStaff(staff);

            DashboardResponse dashboardResponse = new DashboardResponse(dailyAppointments, monthlyAppointments, totalAppointmentInMonth, totalAppointmentInYear);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
