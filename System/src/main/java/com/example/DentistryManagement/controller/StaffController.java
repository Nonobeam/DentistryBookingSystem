package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.Mapping.UserMapping;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.core.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.*;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Staff API")
public class StaffController {
    private final MailService emailService;
    private final UserService userService;
    private final ServiceService serviceService;
    private final DentistService dentistService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final DentistScheduleService dentistScheduleService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final DentistRepository dentistRepository;
    private final TimeSlotService timeSlotService;
    private final StaffService staffService;
    private final UserMapping userMapping;

    //----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Staff information")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> findUser() {
        String mail = userService.mailExtract();
        Client user = userService.findClientByMail(mail);
        return ResponseEntity.ok(userMapping.getUserDTOFromUser(user));
    }


    @Operation(summary = "User update their profile")
    @PutMapping("/info/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            userService.findByMail(userService.mailExtract()).
                    ifPresent(
                            user -> userService.updateUser(userDTO, user)
                    );
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


    //---------------------------MANAGE DENTIST---------------------------


    @Operation(summary = "All Services in Clinic To choose")
    @GetMapping("/show-service")
    public ResponseEntity<?> getServices() {
        try {
            List<Services> services = serviceService.findAllServices();
            if (!services.isEmpty()) {
                return ResponseEntity.ok(services);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any service");
                logger.error("Not found any service");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Set Service for dentists")
    @PostMapping("/set-service/{dentistID}")
    public ResponseEntity<?> updateDentistService(@PathVariable String dentistID, @RequestParam String serviceID) {
        try {
            Dentist dentist = dentistService.findDentistByID(dentistID);
            Services service = serviceService.findServiceByID(serviceID);
            dentist.getServicesList().add(service);
            dentistService.save(dentist);
            return ResponseEntity.ok("Set successful");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList")
    public ResponseEntity<?> findAllDentistByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients;
            if (search != null && !search.isEmpty()) {
                clients = userService.searchDentistByStaff(mail, search);
            } else {
                clients = userService.findDentistByStaff(mail);
            }

            if (!clients.isEmpty()) {
                List<UserDTO> clientDTOs = clients.stream()
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
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any dentist user");
                logger.error("Not found any dentist user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList/{mail}")
    public ResponseEntity<?> findDentistInformationByStaff(@PathVariable("mail") String dentistMail) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(dentistMail);
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());

            List<Appointment> appointmentList = appointmentService.findAllAppointmentByDentist(client.getMail(), staff.getClinic());
            List<AppointmentDTO> appointmentDTOList = appointmentService.appointmentDTOList(appointmentList);

            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "show dentist list timeslot for choosing set schedule")
    @GetMapping("/show-set-schedule")
    public ResponseEntity<?> showSetDentistSchedule() {
        try {
            String mail = userService.mailExtract();
            List<Client> dentistList;
            List<UserDTO> dentistListDTO = new ArrayList<>();
            List<TimeSlotDTO> timeSlotDTOS = new ArrayList<>();
            dentistList = userService.findDentistByStaff(mail);
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (!dentistList.isEmpty()) {
                dentistListDTO = dentistList.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setStatus(client.getStatus());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());
            }
            List<TimeSlot> timeSlotList = timeSlotService.findByClinic(staff.getClinic());
            if (!timeSlotList.isEmpty()) {
                timeSlotDTOS = timeSlotList.stream()
                        .map(timeSlot -> {
                            TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
                            timeSlotDTO.setStartTime(timeSlot.getStartTime());
                            timeSlotDTO.setSlotNumber(timeSlot.getSlotNumber());
                            return timeSlotDTO;
                        }).collect(Collectors.toList());
            }
            SetScheduleRequestDTO setScheduleRequestDTO = new SetScheduleRequestDTO(dentistListDTO, timeSlotDTOS);
            return ResponseEntity.ok(setScheduleRequestDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Set Dentist Schedule")
    @PostMapping("/set-schedule")
    public ResponseEntity<?> setDentistSchedule(@RequestParam String dentistMail,
                                                @RequestParam LocalDate startDate,
                                                @RequestParam LocalDate endDate,
                                                @RequestParam int slotNumber) {
        try {
            String mail = userService.mailExtract();
            if (mail == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "Cannot find user with current mail"), HttpStatus.FORBIDDEN);
            }

            Staff staff = userService.findStaffByMail(mail);
            if (staff == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "You don't have permission to do this"), HttpStatus.FORBIDDEN);
            }

            Clinic clinic = staff.getClinic();
            if (clinic == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "Staff doesn't belong to any clinic"), HttpStatus.FORBIDDEN);
            }

            Dentist dentist = dentistRepository.findDentistByUserMail(dentistMail);
            //Check for the newest Date;
            LocalDate startUpdateTimeSlotDate = timeSlotService.startUpdateTimeSlotDate(dentist.getClinic().getClinicID());
            if (startUpdateTimeSlotDate == null) {
                return ResponseEntity.status(403).body(new ErrorResponseDTO("403", "Cannot find any time slot for this clinic's name: " + dentist.getClinic().getName()));
            }

            if (startUpdateTimeSlotDate.isAfter(startDate) && startUpdateTimeSlotDate.isBefore(endDate)) {
                return new ResponseEntity<>(new ErrorResponseDTO("400", "Must be done this separately. The schedule is must after or before the update timeslot date " + startUpdateTimeSlotDate), HttpStatus.BAD_REQUEST);
            }
            dentistScheduleService.setDentistSchedule(dentist.getDentistID(), startDate, endDate, slotNumber, clinic.getClinicID());
            return ResponseEntity.ok("Schedule set successfully");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    // Missing check if available or not

//    @Operation(summary = "Delete Dentist Schedule")
//    @DeleteMapping("/delete-schedule")
//    public ResponseEntity<?> deleteDentistSchedule(@RequestParam String dentistID,
//                                                   @RequestParam LocalDate workDate, @RequestParam int numSlot) {
//        try {
//            dentistScheduleService.deleteDentistSchedule(dentistID, workDate, numSlot);
//            return ResponseEntity.ok("Schedule deleted successfully");
//        } catch (Exception e) {
//            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
//            logger.error("Server_error", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//        }
//    }


    //---------------------------MANAGE CUSTOMER---------------------------

    @Operation(summary = "All Clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/dependentList/{customerMail}")
    public ResponseEntity<?> getAllDependentByCustomer(@PathVariable String customerMail) {
        try {
            Client customer = userService.findClientByMail(customerMail);
            if (customer != null) {
                List<Dependent> dependentsList = userService.findDependentByCustomer(customer.getMail());
                if (dependentsList.isEmpty()) {
                    return ResponseEntity.ok(new ArrayList<>());
                } else return ResponseEntity.ok(dependentsList);
            } else {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any customer user");
                logger.error("Not found any customer user ");
                return ResponseEntity.status(204).body(error);
            }
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @Operation(summary = "customerList")
    @GetMapping("/customerList")
    public ResponseEntity<?> findAllCustomerByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients;

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
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Not found any customer ");
                logger.error("Not found any customer ");
                return ResponseEntity.status(204).body(error);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Customer Info")
    @GetMapping("/customerList/{mail}")
    public ResponseEntity<?> findCustomerInformationByStaff(@PathVariable("mail") String customerMail) {
        try {
            String staffMail = userService.mailExtract();

            UserDTO userDTO = new UserDTO();
            Client client = userService.findClientByMail(customerMail);
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());
            List<Appointment> appointmentList = appointmentService.customerAppointment(client.getUserID(), staffMail);
            List<AppointmentDTO> appointmentDTOList = appointmentService.appointmentDTOList(appointmentList);
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setCode("400");
            error.setMessage("Server_error");
            logger.error("Server_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //---------------------------MANAGE APPOINTMENT---------------------------


    @Operation(summary = "Send mail for user")
    @PostMapping("/{notificationID}/send-email")
    public ResponseEntity<?> sendEmail(@PathVariable String notificationID,
                                       @RequestParam String mail,
                                       @RequestParam String subject,
                                       @RequestParam String text) {
        Notification optionalNotification = notificationService.findNotificationByIDAndStatus(notificationID, 0);
        if (optionalNotification != null) {

            emailService.sendSimpleMessage(mail, subject, text);
            optionalNotification.setStatus(1);

            notificationService.save(optionalNotification);
            return ResponseEntity.ok("Mail send successfully");
        } else {
            throw new IllegalArgumentException("Notification not found with ID: " + notificationID);
        }
    }


    @Operation(summary = "Get all notification in the clinic")
    @GetMapping("/notification")
    public ResponseEntity<?> receiveNotification() {
        try {
            String mail = userService.mailExtract();

            List<Notification> notice = notificationService.receiveNotice(mail);
            if (notice == null) {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Notification not found");
                logger.error("Notification not found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
            }

            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/booking/all-service")
    public ResponseEntity<?> getAllServiceByClinic(@RequestParam LocalDate workDate) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            List<Services> dentistService;
            dentistService = serviceService
                    .getServiceNotNullByDate(workDate, clinic).stream().toList();
            return ResponseEntity.ok(dentistService);
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }

    @Operation(summary = "Show available schedules")
    @GetMapping("/booking/available-schedules")
    public ResponseEntity<?> getAvailableSchedules(
            @RequestParam LocalDate workDate,
            @RequestParam String servicesId) {
        Staff staff = userService.findStaffByMail(userService.mailExtract());
        Clinic clinic = staff.getClinic();
        List<DentistSchedule> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinic.getClinicID()).stream().toList();

        List<AvailableSchedulesResponse> availableSchedulesResponse = new ArrayList<>();
        for (DentistSchedule i : dentistScheduleList) {
            AvailableSchedulesResponse availableSchedule = new AvailableSchedulesResponse();
            availableSchedule.setDentistScheduleID(i.getScheduleID());
            availableSchedule.setDentistName(i.getDentist().getUser().getName());
            availableSchedule.setStartTime(i.getTimeslot().getStartTime());
            availableSchedule.setWorkDate(workDate);
            availableSchedulesResponse.add(availableSchedule);
        }
        return ResponseEntity.ok(availableSchedulesResponse);
    }


    @Operation(summary = "Booking")
    @PostMapping("/booking/make-booking/{dentistScheduleId}")
    public ResponseEntity<?> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail, @RequestParam String serviceId) {
        try {
            // Current user
            Client staff = userService.findClientByMail(userService.mailExtract());
            Client customer = userService.findClientByMail(customerMail);
            Dependent dependent = dependentID != null ? userService.findDependentByDependentId(dependentID) : null;
            Services services = serviceService.findServiceByID(serviceId);
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);

            if (customer == null || customer.getStatus() == 0 || customer.getRole() != Role.CUSTOMER) {
                ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found in system");
                logger.error("Customer not found in system");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            if (dentistSchedule == null || dentistSchedule.getAvailable() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("400", "Dentist Schedule not found"));
            } else if (dentistSchedule.getWorkDate().isBefore(LocalDate.now())) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "The booking must be in the future"));
            }

            if (services == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("400", "Service not found"));
            }

            if (appointmentService.findAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "Reach the limit of personal appointment. 5/5"));
            }

            if (appointmentService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).size() >= 10) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "You cannot book another appointment right now. The clinic is full right now!"));
            }

            appointmentService.createAppointment(staff, customer, dentistSchedule, services, dependent);
            return ResponseEntity.ok("Booking successfully");
        } catch (Error e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PutMapping("/delete-booking/{appointmentId}")
    public ResponseEntity<?> deleteBooking(@PathVariable String appointmentId) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            String dentistScheduleId = appointment.getDentistScheduleId();
            DentistSchedule dentistSchedule = dentistScheduleService.findByScheduleId(dentistScheduleId);
            if (appointment.getStatus() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment has already been cancelled");
            }
            appointment.setStatus(0);
            Optional<List<DentistSchedule>> unavailableSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 0);
            unavailableSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(1)));
            appointmentService.save(appointment);
            return ResponseEntity.ok("Appointment has been cancelled");
        } catch (Error e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found in system");
            logger.error("Customer not found in stem");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Staff")
    @PatchMapping("/appointment-history/{appointmentId}")
    public ResponseEntity<?> setAppointmentStatus(@PathVariable("appointmentId") String appointmentId, @RequestParam("status") int status) {

        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentService.AppointmentUpdate(appointment));

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/appointment-history")
    public ResponseEntity<?> appointmentHistoryByStaff(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList;
            if (date != null || (search != null && !search.isEmpty())) {
                appointmentList = appointmentService.searchAppointmentByStaff(date, search, mail);
            } else appointmentList = appointmentService.findAppointmentInClinic(mail);
            List<AppointmentDTO> appointmentDTOList = appointmentService.appointmentDTOList(appointmentList);
            if (!appointmentDTOList.isEmpty()) {
                return ResponseEntity.ok(appointmentDTOList);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No appointments found");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found");
            logger.error("Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @Operation(summary = "Staff Dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoardData(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(value = "year", required = false) int year) {
        try {
            Staff staff = userService.findStaffByMail(userService.mailExtract());
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (date == null) date = LocalDate.now();
            if (year == -1) year = LocalDate.now().getYear();
            Map<String, Integer> dailyAppointments = appointmentService.getDailyAppointmentsByDentist(date, staff);
            Map<Integer, Long> monthlyAppointments = appointmentService.getAppointmentsByStaffForYear(staff, year);
            int totalAppointmentInMonth = appointmentService.totalAppointmentsInMonthByStaff(staff);
            int totalAppointmentInYear = appointmentService.totalAppointmentsInYearByStaff(staff);

            DashboardResponse dashboardResponse = new DashboardResponse(dailyAppointments, monthlyAppointments, totalAppointmentInMonth, totalAppointmentInYear);

            return ResponseEntity.ok(dashboardResponse);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Data not found");
            logger.error("Data not found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
        }
    }


    //---------------------------MANAGE CLINIC---------------------------


    @Operation(summary = "Get timetable for 1 specific date")
    @GetMapping("/timetable/{startDate}")
    public ResponseEntity<?> timeTable(@PathVariable("startDate") LocalDate date, @RequestParam int numDay) {
        try {
            Staff staff = staffService.findStaffByMail(userService.mailExtract());
            Map<LocalDate, List<TimeTableResponseDTO>> timeTableResponseMap = new HashMap<>();
            date.datesUntil(date.plusDays(numDay).plusDays(1)).forEach(currentDate -> {
                List<DentistSchedule> dentistSchedules = dentistScheduleService.findDentistScheduleByWorkDate(date, numDay, staff).stream()
                        .filter(schedule -> schedule.getWorkDate().equals(currentDate))
                        .collect(Collectors.toList());
                List<Appointment> appointments = appointmentService.findAppointmentsByDateBetween(date, date.plusDays(numDay), staff).stream()
                        .filter(appointment -> appointment.getDate().equals(currentDate))
                        .collect(Collectors.toList());
                TimeTableResponseDTO timeTableResponseDTO = new TimeTableResponseDTO();
                List<TimeTableResponseDTO> timeTableResponseDTOList = timeTableResponseDTO.getTimeTableResponseDTOList(dentistSchedules, appointments);
                timeTableResponseMap.put(currentDate, timeTableResponseDTOList);
            });

            return ResponseEntity.ok(timeTableResponseMap);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", e.getMessage());
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}

