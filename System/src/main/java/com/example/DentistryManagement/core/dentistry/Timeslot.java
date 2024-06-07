package com.example.DentistryManagement.core.dentistry;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TimeSlot")
public class TimeSlot {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timeSlotID")
    private int timeSlotID;
    @NotBlank(message = "Start time must not be blank")
    private LocalTime startTime;


    @ManyToOne
    @JoinColumn(name = "clinicID", nullable = false, referencedColumnName = "clinicID")
    private Clinic clinic;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSlot")
    private List<Appointment> appointmentList;
}
