package com.example.DentistryManagement.core.mail;

import lombok.Getter;
import lombok.Setter;

public class Notification {

    @Getter
    @Setter
    public class Mail {
        private String subject;
        private String message;
    }

}