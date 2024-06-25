package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DashboardResponse {
    private Map<Client, Integer>dailyAppointments;
    private Map<Integer, Long> monthlyAppointments;
    private int totalAppointmentsInMonthNow;
    private int totalAppointmentsInYearNow;

}
