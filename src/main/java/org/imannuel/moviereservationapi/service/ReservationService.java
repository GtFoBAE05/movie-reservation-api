package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.reservation.ReservationRequest;
import org.imannuel.moviereservationapi.dto.response.payment.PaymentResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationPageResponse;
import org.imannuel.moviereservationapi.dto.response.reservation.ReservationResponse;
import org.imannuel.moviereservationapi.entity.Reservation;

public interface ReservationService {
    PaymentResponse createReservation(ReservationRequest reservationRequest);

    void createSeatListReservation(Reservation reservation, ReservationRequest reservationRequest);

    void createSeatReservation(String reservationId, String seatId);

    boolean checkIsSeatAvailable(String seatId, String showtimeId);

    void cancelReservation(String reservationId);

    Reservation findReservationById(String reservationId);

    ReservationResponse getReservationById(String reservationId);

    ReservationPageResponse getAllReservationByUserId(Integer page, Integer size);

    boolean existsByReservationIdAndUserAccountId(String reservationId, String userAccountId);
}
