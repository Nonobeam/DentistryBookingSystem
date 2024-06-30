package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Clinic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class ClinicDTO {
    private String id;
    private String phone;
    private String name;
    private String address;
    private LocalTime slotDuration;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;

    public ClinicDTO clinicMapping(Clinic clinic) {
        ClinicDTO clinicDTO = new ClinicDTO();
        clinicDTO.setId(clinic.getClinicID());
        clinicDTO.setName(clinic.getName());
        clinicDTO.setPhone(clinic.getPhone());
        clinicDTO.setAddress(clinic.getAddress());
        clinicDTO.setSlotDuration(clinic.getSlotDuration());
        clinicDTO.setOpenTime(clinic.getOpenTime());
        clinicDTO.setCloseTime(clinic.getCloseTime());
        clinicDTO.setBreakStartTime(clinic.getBreakStartTime());
        clinicDTO.setBreakEndTime(clinic.getBreakEndTime());

        return clinicDTO;
    }
}
