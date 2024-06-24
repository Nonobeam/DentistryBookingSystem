package com.example.DentistryManagement.core.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {
    private String to;
    private String subject;
    private String text;
}
