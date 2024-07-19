package com.example.DentistryManagement.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClinicWorkerResponseDTO {
    List<ClinicWorkerDTO> staffList;
    List<ClinicWorkerDTO> dentistList;

}
