package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Dentist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "DentistSchedule")
@Entity
public class DentistSchedule {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dentistScheduleId")
    private String scheduleId;
    private LocalDate dateWork;


    @ManyToOne
    @JoinColumn(name = "dentist_id_fk", referencedColumnName = "dentist_id_fk")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "clinicId_fk", nullable = false, referencedColumnName = "clinicId")
    private Clinic clinicDentistSchedule;

    @ManyToOne
    @JoinColumn(name = "timeSlotId_fk", nullable = false, referencedColumnName = "timeSlotId")
    private Timeslot timeSlotDentistSchedule;

    @ManyToOne
    @JoinColumn(name = "serviceId_fk", nullable = false, referencedColumnName = "serviceId")
    private DentistryService serviceDentistSchedule;

    @Column(name = "available", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int available;
}
