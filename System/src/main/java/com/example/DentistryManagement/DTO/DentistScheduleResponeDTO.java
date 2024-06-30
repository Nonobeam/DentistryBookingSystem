package com.example.DentistryManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DentistScheduleResponeDTO {
    private List<AppointmentDTO> appointmentDTOList;
    private List<AvailableSchedulesResponse> availableSchedulesResponseList;
}
