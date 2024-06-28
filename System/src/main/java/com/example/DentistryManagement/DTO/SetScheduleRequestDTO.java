package com.example.DentistryManagement.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetScheduleRequestDTO {
    private List<UserDTO> dentistList;
    private List<TimeSlotDTO> timeSlotList;
}
