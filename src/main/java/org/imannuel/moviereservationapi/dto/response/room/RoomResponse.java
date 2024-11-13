package org.imannuel.moviereservationapi.dto.response.room;

import lombok.*;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatResponse;
import org.imannuel.moviereservationapi.entity.Seat;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    private Long id;

    private String name;

    private List<SeatResponse> seats;
}
