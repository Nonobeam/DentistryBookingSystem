package com.example.DentistryManagement.DTO;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClinicWorkerDTO {
    private String name;
    private String mail;
    private String clinicName;
    private String clinicAddress;
    private String id;

    public static ClinicWorkerDTO revertClientToWorkerDTO(Client client) {
        ClinicWorkerDTO clinicWorkerDTO = new ClinicWorkerDTO();
        clinicWorkerDTO.setName(client.getName());
        clinicWorkerDTO.setMail(client.getMail());
        clinicWorkerDTO.setId(client.getUserID());
        if (client.getRole() == Role.STAFF) {
            clinicWorkerDTO.setClinicName(client.getStaff().getClinic().getName());
            clinicWorkerDTO.setClinicAddress(client.getStaff().getClinic().getAddress());
        } else if (client.getRole() == Role.DENTIST) {
            clinicWorkerDTO.setClinicName(client.getDentist().getClinic().getName());
            clinicWorkerDTO.setClinicAddress(client.getDentist().getClinic().getAddress());
        }
        return clinicWorkerDTO;
    }

    public static List<ClinicWorkerDTO> fromClientList(List<Client> clients) {
        return clients.stream()
                .map(ClinicWorkerDTO::revertClientToWorkerDTO)
                .collect(Collectors.toList());
    }
}
