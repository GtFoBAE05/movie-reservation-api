package org.imannuel.moviereservationapi.dto.response.reservation;

import lombok.*;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {
    private String id;

    private ShowtimeResponse showtime;

    private SeatListResponse seatList;

    private boolean cancelStatus;
}
