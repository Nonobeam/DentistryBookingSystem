package com.example.DentistryManagement.core.dentistry;

import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Dentist;
import com.example.DentistryManagement.core.user.Staff;
import com.example.DentistryManagement.repository.ClinicRepository;
import com.example.DentistryManagement.repository.TimeSlotRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clinicID")
    private String clinicID;
    @NotBlank
    private String name;
    @NotBlank(message = "Phone number must not be empty")
    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    @Size(min = 8, max = 11, message = "Phone number cannot exceed 11 characters")
    private String phone;
    @NotBlank(message = "Address must not be empty")
    private String address;
    @NotBlank(message = "Slot duration must not be empty")
    private LocalTime slotDuration;
    @NotBlank(message = "Open time must not be empty")
    private LocalTime openTime;
    @NotBlank(message = "Close time must not be empty")
    private LocalTime closeTime;
    @NotBlank(message = "Break start time must not be empty")
    private LocalTime breakStartTime;
    @NotBlank(message = "Break end time must not be empty")
    private LocalTime breakEndTime;
    private int status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false, referencedColumnName = "userID")
    private Client user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private List<Staff> staffList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private List<Dentist> dentistList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private List<TimeSlot> timeSlotList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private List<Appointment> appointmentList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private List<DentistSchedule> dentistScheduleList;

    @JsonIgnore
    @ManyToMany(mappedBy = "clinicList")
    private List<Services> servicesList;

}
