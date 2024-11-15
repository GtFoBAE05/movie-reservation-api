package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationListResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.entity.Reservation;

public interface ReservationService {
    void createReservation(ReservationRequest reservationRequest);

    void createSeatReservation(String reservationId, String seatId);

    boolean checkIsSeatAvailable(String seatId, String showtimeId);

    void cancelReservation(String reservationId);

    boolean checkIsReservationIsCancelable(String reservationId);

    Reservation findReservationById(String reservationId);

    ReservationResponse getReservationById(String reservationId);

    ReservationListResponse getAllReservationByUserId();

    boolean existsByReservationIdAndUserAccountId(String reservationId, String userAccountId);
}
