package com.example.DentistryManagement.core.mail;

import com.example.DentistryManagement.core.user.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Appointment")
@Entity
public class Notification {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notificationId")
    private String notificationId;
    private String message;
    private LocalDateTime createTime;
    private int status;

    @ManyToOne
    @JoinColumn(name = "client_fk", referencedColumnName = "clientId")
    private Client clientNotification;

}
