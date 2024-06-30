package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Appointment;
import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TimeTableResponseDTO {
    private String id;
    private LocalDate date;
    private LocalTime time;
    private String dentistName;
    private String customerName;
    private String serviceName;
    private int status;

    public List<TimeTableResponseDTO> getTimeTableResponseDTOList(List<DentistSchedule> dentistSchedules, List<Appointment> appointments) {
        List<TimeTableResponseDTO> timeTableResponseDTOList = new ArrayList<>();

        for (DentistSchedule dentistSchedule : dentistSchedules) {
            TimeTableResponseDTO timeTableResponseDTO = getTimeTableResponseDTOFromDentistSchedule(dentistSchedule);
            timeTableResponseDTOList.add(timeTableResponseDTO);
        }

        for (Appointment appointment : appointments) {
            TimeTableResponseDTO timeTableResponseDTO = getTimeTableResponseDTOFromAppointment(appointment);
            timeTableResponseDTOList.add(timeTableResponseDTO);
        }

        return timeTableResponseDTOList;
    }


    public TimeTableResponseDTO getTimeTableResponseDTOFromDentistSchedule(DentistSchedule dentistSchedule) {
        TimeTableResponseDTO timeTableResponseDTO = new TimeTableResponseDTO();

        if (dentistSchedule.getAvailable() != 0) {
            timeTableResponseDTO.setId(dentistSchedule.getScheduleID());
            timeTableResponseDTO.setDate(dentistSchedule.getWorkDate());
            timeTableResponseDTO.setTime(dentistSchedule.getTimeslot().getStartTime());
            timeTableResponseDTO.setDentistName(dentistSchedule.getDentist().getUser().getName());
            timeTableResponseDTO.setCustomerName(null);
            timeTableResponseDTO.setServiceName(null);
            timeTableResponseDTO.setStatus(1);
        }

        return timeTableResponseDTO;
    }


    public TimeTableResponseDTO getTimeTableResponseDTOFromAppointment(Appointment appointment) {
        TimeTableResponseDTO timeTableResponseDTO = new TimeTableResponseDTO();

        if (appointment.getStatus() != 0) {
            timeTableResponseDTO.setId(appointment.getAppointmentID());
            timeTableResponseDTO.setDate(appointment.getDate());
            timeTableResponseDTO.setTime(appointment.getTimeSlot().getStartTime());
            timeTableResponseDTO.setDentistName(appointment.getDentist().getUser().getName());
            timeTableResponseDTO.setCustomerName(appointment.getUser().getName());
            timeTableResponseDTO.setServiceName(appointment.getServices().getName());
            timeTableResponseDTO.setStatus(appointment.getStatus());
        }

        return timeTableResponseDTO;
    }
}
