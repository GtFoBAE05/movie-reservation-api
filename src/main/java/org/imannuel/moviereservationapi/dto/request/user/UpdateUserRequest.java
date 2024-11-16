package org.imannuel.moviereservationapi.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "email is required")
    private String email;
}
