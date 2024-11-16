package org.imannuel.moviereservationapi.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;
}
