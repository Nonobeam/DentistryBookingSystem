package com.example.DentistryManagement.core.mail;

import com.example.DentistryManagement.core.user.Dentist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Notification")
@Entity
public class Notification {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notificationID")
    private String notificationID;
    private String message;
    @JsonIgnore
    private Date date;
    @JsonIgnore
    private Time time;
    @JsonIgnore
    private int status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "dentistID", referencedColumnName = "dentistID")
    private Dentist dentist;

}
