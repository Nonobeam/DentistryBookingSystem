package com.example.DentistryManagement.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class UserAppointDTO {
    private UserDTO userDTO;
    private List<AppointmentDTO> appointment;
}
