package com.example.DentistryManagement.DTO;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor

public class DashboardResponse {
    private Map<String, Integer>dailyAppointments;
    private Map<Integer, Long> monthlyAppointments;
    private int totalAppointmentsInMonthNow;
    private int totalAppointmentsInYearNow;

}
