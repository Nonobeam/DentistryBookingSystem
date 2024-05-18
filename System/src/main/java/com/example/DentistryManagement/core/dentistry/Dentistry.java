package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Dentistry")
public class Dentistry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String dentistryId;

    private String name;
    private String address;
    private String phone;
    private List<String> dentists;


}
