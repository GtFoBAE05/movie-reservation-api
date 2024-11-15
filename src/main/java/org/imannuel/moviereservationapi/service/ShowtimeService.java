package org.imannuel.moviereservationapi.service;

import org.imannuel.moviereservationapi.dto.request.showtime.ShowtimeRequest;
import org.imannuel.moviereservationapi.dto.response.Seat.SeatListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeListResponse;
import org.imannuel.moviereservationapi.dto.response.showtime.ShowtimeResponse;
import org.imannuel.moviereservationapi.entity.Showtime;

public interface ShowtimeService {
    void createShowtime(ShowtimeRequest showtimeRequest);

    void updateShowtime(String id, ShowtimeRequest showtimeRequest);

    Showtime findShowtimeById(String id);

    ShowtimeResponse getShowtimeById(String id);

    ShowtimeListResponse getAllShowtime();

    ShowtimeListResponse getAllHistoryShowtime();

    ShowtimeListResponse getShowtimeBy(String date, String movieId);

    boolean checkIsShowtimeUpdateable(String id);

    SeatListResponse getAvailableSeat(String showtimeId);

    boolean checkIsShowtimeIsReserveable(String showtimeId);
}
