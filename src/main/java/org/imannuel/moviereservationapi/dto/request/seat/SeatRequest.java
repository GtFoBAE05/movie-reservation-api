package org.imannuel.moviereservationapi.dto.request.seat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatRequest {
    private String seatCode;

    private Long roomId;
}
