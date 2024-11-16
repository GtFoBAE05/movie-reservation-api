package org.imannuel.moviereservationapi.dto.mapper;

import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.entity.Reservation;

import java.util.List;

public class ReservationMapper {
    public static ReservationResponse reservationToReservationResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId().toString())
                .showtime(ShowtimeMapper.showtimeToShowtimeResponse(reservation.getShowtime()))
                .seatList(SeatMapper.seatListToSeatListResponse(reservation.getSeats()))
                .cancelStatus(reservation.getIsCancel())
                .build();
    }

    public static List<ReservationResponse> reservationListToReservationListResponse(List<Reservation> reservations) {
        return reservations.stream().map(reservation -> reservationToReservationResponse(reservation)).toList();
    }
}
