package com.example.DentistryManagement.core.token;

import com.example.DentistryManagement.core.user.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String passwordResetTokenID;
    private String token;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false, referencedColumnName = "userID")
    private Client user;

    private LocalDateTime expiryTime;
}
