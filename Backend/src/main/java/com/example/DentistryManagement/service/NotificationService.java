package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public List<Notification> receiveNotice(String staffMail) {
        try {
            return notificationRepository.getNotificationByDentist_StaffUserMailAndStatus(staffMail, 1);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while fetching all users: " + e.getMessage(), e);
        }
    }

    public Notification insertNotification(Notification notification) {
        try {
            return notificationRepository.save(notification);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error occurred while create notification: " + e.getMessage(), e);
        }
    }

    public Notification findNotificationByIDAndStatus(String notificationID, int status) {
        try {
            return notificationRepository.getNotificationByNotificationIDAndStatus(notificationID, status);
        } catch (Error e) {
            throw e;
        }
    }

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }
}
