package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.seat.SeatRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.entity.Seat;

import java.util.List;

public interface SeatService {
    void createSeat(SeatRequest seatRequest);

    Seat findSeatById(String id);

    SeatListResponse findSeatByRoomId(Long id);

    void updateSeat(String id, SeatRequest seatRequest);

    void deleteSeat(String id);

    boolean checkIsSeatExists(String seatId, Long roomId);

    List<Seat> getAvailableSeatForShowtime(String showtimeId);
}
