package org.imannuel.moviereservationapi.dto.response.Seat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatResponse {
    private String id;

    private String seatCode;
}
