package com.example.DentistryManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DentistScheduleResponeDTO {
    private List<AppointmentDTO> appointmentDTOList;
    private List<AvailableSchedulesResponse> availableSchedulesResponseList;

    public static DentistScheduleResponeDTO fromData(LocalDate date,
                                                     List<AvailableSchedulesResponse> availableSchedules,
                                                     List<AppointmentDTO> appointments) {
        DentistScheduleResponeDTO responseDTO = new DentistScheduleResponeDTO();
        responseDTO.setAvailableSchedulesResponseList(availableSchedules);
        responseDTO.setAppointmentDTOList(appointments);
        return responseDTO;
    }
}
