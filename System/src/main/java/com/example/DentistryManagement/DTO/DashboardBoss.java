package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.Clinic;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor

public class DashboardBoss {
    private Map<String, List<Appointment>> dailyAppointments;
    private Map<String, Map<Integer, Long>>  monthlyAppointments;
    private int totalAppointmentsInMonthNow;
    private int totalAppointmentsInYearNow;

}
