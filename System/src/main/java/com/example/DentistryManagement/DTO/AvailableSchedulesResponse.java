package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.DentistSchedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AvailableSchedulesResponse {
    private String dentistName;
    private LocalTime startTime;
    private String dentistScheduleID;
    private LocalDate workDate;

    public static AvailableSchedulesResponse ScheduleConvert(DentistSchedule dentistSchedule){
        AvailableSchedulesResponse ScheduleConvert = new AvailableSchedulesResponse();
        ScheduleConvert.setDentistScheduleID(dentistSchedule.getScheduleID());
        ScheduleConvert.setDentistName(dentistSchedule.getDentist().getUser().getName());
        ScheduleConvert.setStartTime(dentistSchedule.getTimeslot().getStartTime());
        ScheduleConvert.setWorkDate(dentistSchedule.getWorkDate());  // Set the workDate

        return ScheduleConvert;
    }
}
