package org.imannuel.moviereservationapi.dto.request.reservation;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {
    private String showtimeId;

    private List<String> seatId;
}
