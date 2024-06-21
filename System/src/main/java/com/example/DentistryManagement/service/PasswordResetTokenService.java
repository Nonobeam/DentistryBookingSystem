package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.passwordResetToken.PasswordResetToken;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.repository.PasswordResetTokenRepository;
import com.example.DentistryManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String fromMail;

    public void createPasswordResetTokenForUser(Client user, String token) {
        PasswordResetToken
                myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryTime(calculateExpiryDate());
        tokenRepository.save(myToken);
    }

    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken == null || isTokenExpired(passToken)) {
            return "invalid";
        }
        return "valid";
    }

    public void resetPassword(String token, String password){
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        Client user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        tokenRepository.delete(passToken); // Invalidate the used token
    }

    public void sendPasswordResetEmail(String mail, String token) {
        String url = "http://localhost:8080/resetPassword/" + token;
        SimpleMailMessage resetMessage = new SimpleMailMessage();
        resetMessage.setFrom(fromMail);
        resetMessage.setSubject("Password Reset Request");
        resetMessage.setText("To reset your password, click the link below:\n" + url + "\nThis link will expire after 5 minutes");
        resetMessage.setTo(mail);
        mailSender.send(resetMessage);
    }

    private LocalDateTime calculateExpiryDate() {
        LocalDateTime expiryDate = LocalDateTime.now();
        return expiryDate.plusMinutes(5);
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpiryTime().isBefore(LocalDateTime.now());
    }
}
