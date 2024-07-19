package com.example.DentistryManagement.core.dentistry;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "timeSlotID")
    private String timeSlotID;
    private int slotNumber;
    @NotBlank(message = "Date must not be blank")
    private LocalDate date;
    @NotBlank(message = "Start time must not be blank")
    private LocalTime startTime;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clinicID", referencedColumnName = "clinicID")
    private Clinic clinic;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSlot")
    private List<Appointment> appointmentList;
}
