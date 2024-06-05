package com.example.DentistryManagement.core.dentistry;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Timeslot")
public class Timeslot {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "timeSlotId")
    private int timeSlotId;
    @NotBlank(message = "Start time must not be blank")
    private LocalTime startTime;

    @ManyToOne
    @JoinColumn(name = "clinicId_fk", nullable = false, referencedColumnName = "clinicId")
    private Clinic clinicTimeSlot;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSlotDentistSchedule")
    private List<DentistSchedule> timeSlotDentistScheduleList;
}
