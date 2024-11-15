package org.imannuel.moviereservationapi.dto.response.reservation;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationListResponse {
    private List<ReservationResponse> reservations;
}
