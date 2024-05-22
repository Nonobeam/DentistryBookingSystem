package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Clinic")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String clinicId;
    private String address;
    private String phone;
    private Double duration;
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private LocalTime workStart;
    private LocalTime workEnd;

    @OneToMany
    private List<Client> dentistList;
}
