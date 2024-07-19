package com.example.DentistryManagement.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AppointmentFeedbackDTO {
    private int starAppointment;
    private String feedback;
}
