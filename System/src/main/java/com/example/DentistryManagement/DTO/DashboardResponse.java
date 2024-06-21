package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Appointment;
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
    private Map<String, List<Appointment>> dailyAppointments;
    private Map<Integer, Long> monthlyAppointments;


}