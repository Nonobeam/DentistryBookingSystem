
package com.example.DentistryManagement.auth;

import com.example.DentistryManagement.core.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private Role role;
}
