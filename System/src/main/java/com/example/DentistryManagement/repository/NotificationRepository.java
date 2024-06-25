package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.user.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> getNotificationByDentist_StaffUserMail(String staffmail);

    List<Notification> getNotificationsByDentistAndStatus(Dentist dentist, int status);

    Notification getNotificationByNotificationIDAndStatus(String notificationID, int status);
}
