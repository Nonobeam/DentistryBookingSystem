package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.dentistry.Clinic;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime slotDuration;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime openTime;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime closeTime;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime breakStartTime;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime breakEndTime;
    private int status;

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
        clinicDTO.setStatus(clinic.getStatus());
        return clinicDTO;
    }
}
