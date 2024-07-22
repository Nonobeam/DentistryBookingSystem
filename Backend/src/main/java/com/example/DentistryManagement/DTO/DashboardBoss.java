package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Appointment;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor

public class DashboardBoss {
    private Map<String, Integer> dailyAppointments;
    private Map<String, Map<Integer, Long>>  monthlyAppointments;
    private int totalAppointmentsInMonthNow;
    private int totalAppointmentsInYearNow;
    private Map<String, Double>ratingDentist;


}
