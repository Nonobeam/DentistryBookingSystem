package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Appointment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
public class UserAppoint {
    private UserDTO userDTO;
    private Optional<List<Appointment>>  appointment;
}
