package com.example.DentistryManagement.config.error;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private String code;
    private String message;
}
