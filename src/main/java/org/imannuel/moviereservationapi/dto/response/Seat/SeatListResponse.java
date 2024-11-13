package org.imannuel.moviereservationapi.dto.response.Seat;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatListResponse {
    private List<SeatResponse> seats;
}
