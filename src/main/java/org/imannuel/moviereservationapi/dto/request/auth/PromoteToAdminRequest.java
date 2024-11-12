package org.imannuel.moviereservationapi.dto.request.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoteToAdminRequest {
    private String accountId;
}
