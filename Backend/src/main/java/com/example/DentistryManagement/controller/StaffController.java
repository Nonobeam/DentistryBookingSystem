package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.DTO.*;
import com.example.DentistryManagement.mapping.UserMapping;
import com.example.DentistryManagement.core.dentistry.*;
import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.*;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.service.*;
import com.example.DentistryManagement.service.AppointmentService.AppointmentAnalyticService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentBookingService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentService;
import com.example.DentistryManagement.service.AppointmentService.AppointmentUpdateService;
import com.example.DentistryManagement.service.UserService.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/staff")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Staff API")
public class StaffController {
    private final UserService userService;
    private final UserStaffService userStaffService;
    private final UserDentistService userDentistService;
    private final UserMapping userMapping;
    private final ServiceService serviceService;
    private final TimeSlotService timeSlotService;
    private final DentistRepository dentistRepository;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final DentistScheduleService dentistScheduleService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AppointmentAnalyticService appointmentAnalyticService;
    private final AppointmentBookingService appointmentBookingService;
    private final AppointmentUpdateService appointmentUpdateService;
    private final UserDependentService userDependentService;
    private final UserCustomerService userCustomerService;


    //----------------------------------- USER INFORMATION -----------------------------------

    @Operation(summary = "Staff information")
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

    @Operation(summary = "Staff clinic")
    @GetMapping("/clinic")
    public ResponseEntity<String> clinicName() {
        String mail = userService.mailExtract();
        Client user = userService.findUserByMail(mail);
        return ResponseEntity.ok(user.getStaff().getClinic().getName() + " - " + user.getStaff().getClinic().getAddress());
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
    @PostMapping("/set-service/{dentistMail}")
    public ResponseEntity<?> updateDentistService(@PathVariable String dentistMail, @RequestParam String serviceID) {
        try {
            Dentist dentist = userDentistService.findDentistByMail(dentistMail);
            Services service = serviceService.findServiceByID(serviceID);
            if (!dentist.getServicesList().contains(service)) {
                dentist.getServicesList().add(service);
                userDentistService.save(dentist);
            }
            return ResponseEntity.ok(service.getName());
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList")
    public ResponseEntity<?> findAllDentists(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Client> clients;
            if (search != null && !search.isEmpty()) {
                clients = userDentistService.searchDentistByStaff(mail, search);
            } else {
                clients = userDentistService.findDentistListByStaff(mail);
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
                            clientDTO.setId(client.getUserID());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            } else {
                return ResponseEntity.ok(clients);
            }
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Staff")
    @GetMapping("/dentistList/{mail}")
    public ResponseEntity<?> findDentistInformation(@PathVariable("mail") String dentistMail) {
        try {
            UserDTO userDTO = new UserDTO();
            Client client = userService.findUserByMail(dentistMail);
            Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());

            List<Appointment> appointmentList = appointmentAnalyticService.getAppointmentsInAClinicByCustomerMail(client.getMail(), staff.getClinic());
            List<AppointmentDTO> appointmentDTOList = appointmentService.appointmentDTOList(appointmentList);
            UserAppointDTO userAppointDTO = new UserAppointDTO();
            userAppointDTO.setUserDTO(userDTO);
            List<String> services = client.getDentist().getServicesList().stream().map(Services::getName).toList();
            userAppointDTO.setServices(services.toString());
            userAppointDTO.setStar(appointmentAnalyticService.totalStarByDentist(client));
            userAppointDTO.setAppointment(appointmentDTOList);
            return ResponseEntity.ok(userAppointDTO);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    @Operation(summary = "Show all working dentist schedule")
    @GetMapping("/dentist-schedule")
    public ResponseEntity<?> getWorkingDentistSchedule() {
        try {
            // find current staff account
            Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
            HashSet<DentistSchedule> dentistSchedules = new HashSet<>();
            // Gt all dentists by current staff account
            List<Dentist> dentistList = userDentistService.findDentistListByStaff(staff);

            // Get all dentistSchedule from the dentists
            for (Dentist dentist : dentistList) {
                List<DentistSchedule> dentistSchedule = dentist.getDentistScheduleList().stream().filter(schedule -> schedule.getWorkDate().isAfter(LocalDate.now()) || schedule.getWorkDate().isEqual(LocalDate.now())).toList();

                if (!dentistSchedule.isEmpty()) {
                    dentistSchedules.addAll(dentistSchedule);
                }
            }

            List<DentistSchedule> sortedDentistSchedules = dentistSchedules.stream()
                    .sorted(Comparator.comparing(DentistSchedule::getWorkDate))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(sortedDentistSchedules);
        } catch (Error error) {
            // Get error return from service
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("400", error.getMessage());
            logger.error(errorResponseDTO.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
        }
    }

    @Operation(summary = "Show dentists and timeslots for choosing set schedule")
    @GetMapping("/show-set-schedule")
    public ResponseEntity<?> showDentistsAndDentistsTimeSlots(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            // Initial 2 return lists
            List<UserDTO> dentistListDTO = new ArrayList<>();
            List<TimeSlotDTO> timeSlotDTOS = new ArrayList<>();

            // Get current staff account
            List<Client> dentistList;
            Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
            Clinic clinic = staff.getClinic();
            // Get all dentists by theirs Staff
            dentistList = userDentistService.findDentistListByStaff(userService.mailExtract());
            // Put all dentists in dentistList  ---->  dentistListDTO
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

            HashSet<TimeSlot> timeSlotList = new HashSet<>();
            LocalDate sub = startDate;
            while (sub.isEqual(endDate) || sub.isBefore(endDate)) {
                LocalDate timeSlot = timeSlotService.findNearestTimeSlot(sub, clinic.getClinicID());
                timeSlotList.addAll(timeSlotService.getTimeSlotByDate(clinic, timeSlot));
                sub = sub.plusDays(1);
            }

            // Put all time slot in clinic  ---->  timeslotListDTO
            for (TimeSlot timeSlot : timeSlotList) {
                for (TimeSlot timeSlotDTO : timeSlotList) {
                    if (timeSlotDTO.getDate().isAfter(timeSlot.getDate())) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You have to set schedule  for dentist specific before " + timeSlot.getDate() + " or after " + timeSlotDTO.getDate());
                    }
                }
            }
            if (!timeSlotList.isEmpty()) {
                timeSlotDTOS = timeSlotList.stream()
                        .map(timeSlot -> {
                            TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
                            timeSlotDTO.setStartTime(timeSlot.getStartTime());
                            timeSlotDTO.setSlotNumber(timeSlot.getSlotNumber());

                            return timeSlotDTO;
                        }).sorted(Comparator.comparingInt(TimeSlotDTO::getSlotNumber))
                        .collect(Collectors.toList());

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

            Staff staff = userStaffService.findStaffByMail(mail);
            if (staff == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "You don't have permission to do this"), HttpStatus.FORBIDDEN);
            }

            Clinic clinic = staff.getClinic();
            if (clinic == null) {
                return new ResponseEntity<>(new ErrorResponseDTO("403", "Staff doesn't belong to any clinic"), HttpStatus.FORBIDDEN);
            }

            Dentist dentist = dentistRepository.findDentistByUserMail(dentistMail);

            dentistScheduleService.setDentistSchedule(dentist.getDentistID(), startDate, endDate, slotNumber, clinic.getClinicID());
            return ResponseEntity.ok("Schedule set successfully");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("500", e.getMessage());
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @Operation(summary = "Delete Dentist Schedule")
    @DeleteMapping("/delete-schedule/{scheduleID}")
    public ResponseEntity<?> deleteDentistSchedule(@PathVariable String scheduleID
    ) {
        try {
            dentistScheduleService.deleteDentistSchedule(scheduleID);
            return ResponseEntity.ok("Schedule deleted successfully");
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", e.getMessage());
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(error);
        }
    }


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
            Client customer = userService.findUserByMail(customerMail);
            if (customer != null) {
                List<Dependent> dependentsList = userDependentService.findDependentByCustomer(customer.getMail());
                if (dependentsList.isEmpty()) {
                    return ResponseEntity.ok(new ArrayList<>());
                } else return ResponseEntity.ok(dependentsList);
            } else {
                return ResponseEntity.ok(new ArrayList<>());

            }
        } catch (Error error) {
            throw new Error("Error while getting clinic " + error);
        }
    }
    @Operation(summary = "customer list")
    @GetMapping("/allCustomer")
    public ResponseEntity<?> allCustomer() {
        try {
            return ResponseEntity.ok(userCustomerService.findAllCustomer());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("500", e.getMessage()));
        }
    }
    @Operation(summary = "customerList")
    @GetMapping("/customerList")
    public ResponseEntity<?> findAllCustomerByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            HashSet<Client> clients;

            if (search != null && !search.isEmpty()) {
                clients = userCustomerService.searchCustomerInClinicByStaff(mail, search);
            } else {
                clients = userCustomerService.findCustomerInClinicByStaff(mail);
            }

            if (clients != null && !clients.isEmpty()) {
                List<UserDTO> clientDTOs = clients.stream()
                        .map(client -> {
                            UserDTO clientDTO = new UserDTO();
                            clientDTO.setName(client.getName());
                            clientDTO.setPhone(client.getPhone());
                            clientDTO.setMail(client.getMail());
                            clientDTO.setBirthday(client.getBirthday());
                            clientDTO.setId(client.getUserID());
                            return clientDTO;
                        })
                        .collect(Collectors.toList());

                return ResponseEntity.ok(clientDTOs);
            } else {
                return ResponseEntity.ok(clients);

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
            Client client = userService.findUserByMail(customerMail);
            userDTO.setName(client.getName());
            userDTO.setPhone(client.getPhone());
            userDTO.setMail(client.getMail());
            userDTO.setBirthday(client.getBirthday());
            List<Appointment> appointmentList = appointmentAnalyticService.getCustomerAppointmentsInAClinicByStaffMailAndCustomerId(client.getUserID(), staffMail);
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

            notificationService.sendSimpleMessage(mail, subject, text);
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
            Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
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
        Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
        Clinic clinic = staff.getClinic();
        List<DentistSchedule> dentistScheduleList = dentistScheduleService
                .getByWorkDateAndServiceAndAvailableAndClinic(workDate, servicesId, 1, clinic.getClinicID()).stream().toList();

        List<AvailableSchedulesResponse> availableSchedulesResponse = new ArrayList<>();
        for (DentistSchedule i : dentistScheduleList) {
            AvailableSchedulesResponse availableSchedule = new AvailableSchedulesResponse();
            availableSchedule.setDentistScheduleID(i.getScheduleID());
            availableSchedule.setDentistName(i.getDentist().getUser().getName());
            availableSchedule.setStartTime(i.getTimeslot().getStartTime());
            long durationInMinutes = clinic.getSlotDuration().toSecondOfDay() / 60;
            availableSchedule.setEndTime(availableSchedule.getStartTime().plusMinutes(durationInMinutes));
            availableSchedulesResponse.add(availableSchedule);
        }
        return ResponseEntity.ok(availableSchedulesResponse.stream().sorted(Comparator.comparing(AvailableSchedulesResponse::getStartTime)).collect(Collectors.toList()));
    }

    @Operation(summary = "Check maxed booking")
    @GetMapping("/booking")
    public boolean checkMaxedBooking(@RequestParam String mail) {
        Client customer = userService.findUserByMail(mail);
        return appointmentAnalyticService.getAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5;
    }


    @Operation(summary = "Booking")
    @PostMapping("/booking/make-booking/{dentistScheduleId}")
    public ResponseEntity<?> makeBooking(@PathVariable String dentistScheduleId, @RequestParam(required = false) String dependentID, @RequestParam String customerMail, @RequestParam String serviceId) {
        // Apply redis single-thread
        String lockKey = "booking:lock:" + dentistScheduleId;
        boolean lockAcquired = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS));
        if (!lockAcquired) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO("409", "Booking in progress by another user"));
        }

        try {
            // Current user
            Client staff = userService.findUserByMail(userService.mailExtract());
            Client customer = userService.findUserByMail(customerMail);
            Dependent dependent = dependentID != null ? userDependentService.findDependentByDependentId(dependentID) : null;
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

            if (appointmentAnalyticService.getAppointmentsByUserAndStatus(customer, 1).map(List::size).orElse(5) >= 5) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "Reach the limit of personal appointment. 5/5"));
            }

            if (appointmentAnalyticService.findAppointmentsByDateAndStatus(dentistSchedule.getWorkDate(), 1).size() >= 10) {
                return ResponseEntity.status(400).body(new ErrorResponseDTO("400", "You cannot book another appointment right now. The clinic is full right now!"));
            }

            appointmentBookingService.createAppointment(staff, customer, dentistSchedule, services, dependent);
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
            Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentId);
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
            Appointment appointment = appointmentAnalyticService.getAppointmentById(appointmentId);
            appointment.setStatus(status);
            return ResponseEntity.ok(appointmentUpdateService.UpdateAppointment(appointment));

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("400", "Server_error");
            logger.error("Server_error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Staff")
    @GetMapping("/appointment-history")
    public ResponseEntity<?> appointmentHistoryByStaff(@RequestParam(required = false) String search) {
        try {
            String mail = userService.mailExtract();
            List<Appointment> appointmentList;
            if (search != null && !search.isEmpty()) {
                appointmentList = appointmentAnalyticService.getAppointmentsByCustomerNameOrDependentNameAndStaffMail(search, mail);
            } else {
                appointmentList = appointmentAnalyticService.getAppointmentsInAClinicByStaffMail(mail);
            }
            List<AppointmentDTO> appointmentDTOList = appointmentService.appointmentDTOList(appointmentList);
            return ResponseEntity.ok(appointmentDTOList);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("204", "Customer not found");
            logger.error("Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @Operation(summary = "Staff Dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoardData(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "year", required = false) Integer year) {
        try {
            Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (date == null) date = LocalDate.now();
            if (year == null) year = LocalDate.now().getYear();
            Map<String, Integer> dailyAppointments = appointmentAnalyticService.getAppointmentsByDateAndDentist(date, staff);
            Map<Integer, Long> monthlyAppointments = appointmentAnalyticService.getAppointmentsByYearAndStaff(staff, year);
            int totalAppointmentInMonth = appointmentAnalyticService.totalAppointmentsInMonthByStaff(staff);
            int totalAppointmentInYear = appointmentAnalyticService.totalAppointmentsInYearByStaff(staff);
            Map<String, Double> ratingDentist = appointmentAnalyticService.getRatingDentistByStaff(staff);
            DashboardResponse dashboardResponse = new DashboardResponse(dailyAppointments, monthlyAppointments, totalAppointmentInMonth, totalAppointmentInYear, ratingDentist);
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
            Staff staff = userStaffService.findStaffByMail(userService.mailExtract());
            Map<LocalDate, List<TimeTableResponseDTO>> timeTableResponseMap = new HashMap<>();
            date.datesUntil(date.plusDays(numDay).plusDays(1)).forEach(currentDate -> {
                List<DentistSchedule> dentistSchedules = dentistScheduleService.findDentistScheduleByWorkDate(date, numDay, staff).stream()
                        .filter(schedule -> schedule.getWorkDate().equals(currentDate))
                        .sorted(Comparator.comparing(schedule -> schedule.getTimeslot().getStartTime()))
                        .collect(Collectors.toList());
                List<Appointment> appointments = appointmentAnalyticService.findAppointmentsByDateAndStaff(date, date.plusDays(numDay), staff).stream()
                        .filter(appointment -> appointment.getDate().equals(currentDate))
                        .sorted(Comparator.comparing(appointment -> appointment.getTimeSlot().getStartTime()))
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

