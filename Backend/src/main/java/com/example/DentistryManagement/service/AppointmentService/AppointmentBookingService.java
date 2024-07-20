package com.example.DentistryManagement.service.AppointmentService;

import com.example.DentistryManagement.config.error.ErrorResponseDTO;
import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.core.dentistry.TimeSlot;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dependent;
import com.example.DentistryManagement.repository.AppointmentRepository;
import com.example.DentistryManagement.service.DentistScheduleService;
import com.example.DentistryManagement.service.TimeSlotService;
import com.example.DentistryManagement.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentBookingService {
    private final DentistScheduleService dentistScheduleService;
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final TimeSlotService timeSlotService;


    /**
     * @param staff           Input Client staff
     * @param customer        Input Client customer
     * @param dentistSchedule Input DentistSchedule
     * @param services        Input Services
     * @param dependent       Input Dependent dependent
     */
    public void createAppointment(Client staff, Client customer, DentistSchedule dentistSchedule, Services services, Dependent dependent) {
        Appointment.AppointmentBuilder appointmentBuilder = Appointment.builder()
                .user(customer)
                .clinic(dentistSchedule.getClinic())
                .date(dentistSchedule.getWorkDate())
                .timeSlot(dentistSchedule.getTimeslot())
                .dentist(dentistSchedule.getDentist())
                .services(services)
                .dentistScheduleId(dentistSchedule.getScheduleID())
                .status(1);

        if (staff != null) {
            appointmentBuilder.staff(staff.getStaff());
        }

        if (dependent != null) {
            appointmentBuilder.dependent(dependent);
        }
        appointmentBuilder.build();

        boolean alreadyBooked = appointmentRepository.existsByDependentAndUserAndTimeSlotAndStatus(dependent, customer, dentistSchedule.getTimeslot(), 1);
        if (alreadyBooked) {
            throw new Error("Already have appointment in this time");
        }

        // Customer booked an appointment in that timeslot in that date already
        dentistScheduleService.setAvailableDentistSchedule(dentistSchedule, 0);
        Optional<List<DentistSchedule>> otherSchedule = dentistScheduleService.findDentistScheduleByWorkDateAndTimeSlotAndDentist(dentistSchedule.getTimeslot(), dentistSchedule.getWorkDate(), dentistSchedule.getDentist(), 1);
        otherSchedule.ifPresent(schedules -> schedules.forEach(schedule -> schedule.setAvailable(0)));

        appointmentRepository.save(appointmentBuilder.build());
    }


    public boolean isBookedByCustomerIdAndTimeSlotIdAndDate(String customerId, String timeSlotId, LocalDate bookDate) {
        Client appointmentCustomer = userService.findUserById(customerId);
        TimeSlot appointmentTimeSlot = timeSlotService.getTimeSlotByTimeSlotId(timeSlotId);

        return appointmentRepository.existsByTimeSlotAndDateAndUser(appointmentTimeSlot, bookDate, appointmentCustomer);
    }


}
