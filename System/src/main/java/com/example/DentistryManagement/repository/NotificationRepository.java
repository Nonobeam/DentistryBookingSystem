package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.notification.Notification;
import com.example.DentistryManagement.core.user.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> getNotificationByDentist_StaffUserMailAndStatus(String staffMail,int status);

    Notification getNotificationByNotificationIDAndStatus(String notificationID, int status);
}
