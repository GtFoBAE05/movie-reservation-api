package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatResponse;
import org.imannuel.moviereservationapi.entity.Seat;

import java.util.List;

public class SeatMapper {
    public static SeatResponse seatToSeatResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId().toString())
                .seatCode(seat.getSeatCode())
                .build();
    }

    public static SeatListResponse seatListToSeatListResponse(List<Seat> seats) {
        return SeatListResponse.builder()
                .seats(seats.stream().map(
                        seat -> seatToSeatResponse(seat)
                ).toList())
                .build();
    }
}
