package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Manager;
import com.example.DentistryManagement.core.user.Staff;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Time;
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
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clinicId")
    private String clinicId;

    @NotBlank(message = "Address must not be empty")
    private String address;
    @NotBlank(message = "Phone must not be empty")
    private String phone;
    @NotBlank(message = "Slot duration must not be empty")
    private Time slotDuration;
    @NotBlank(message = "Break start time must not be empty")
    private LocalTime breakStartTime;
    @NotBlank(message = "Break end time must not be empty")
    private LocalTime breakEndTime;
    @NotBlank(message = "Open time must not be empty")
    private LocalTime openTime;
    @NotBlank(message = "Close time must not be empty")
    private LocalTime closeTime;

    @ManyToOne
    @JoinColumn(name = "managerId_fk", nullable = false, referencedColumnName = "managerId")
    private Manager manager;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicForStaff")
    private List<Staff> staffList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicForDentist")
    private List<Dentist> dentistList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicTimeSlot")
    private List<Timeslot> timeslotList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicAppointment")
    private List<Appointment> clinicAppointmentList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicDentistSchedule")
    private List<DentistSchedule> clinicDentistScheduleList;
}
