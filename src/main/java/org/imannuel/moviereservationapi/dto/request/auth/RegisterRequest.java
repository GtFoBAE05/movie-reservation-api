package org.imannuel.moviereservationapi.dto.request.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String username;

    private String email;

    private String password;
}
